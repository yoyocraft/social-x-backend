package com.youyi.domain.ugc.core;

import com.youyi.common.constant.UgcConstant;
import com.youyi.common.type.cache.CacheKey;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.HotUgcCacheInfo;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofHotUgcListKey;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/25
 */
@Component
@RequiredArgsConstructor
public class UgcHotListJob implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UgcHotListJob.class);

    private final UgcRepository ugcRepository;
    private final CommentaryRepository commentaryRepository;

    private final CacheManager cacheManager;

    private final ReentrantLock jobLock = new ReentrantLock();

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        executeHotJob();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void hotUgcJob() {
        executeHotJob();
    }

    private void calculateAndCacheHotList() {
        for (UgcType type : UgcConstant.hotUgcTypeList) {
            calculateAndCacheHotList(type);
        }
    }

    private void calculateAndCacheHotList(UgcType ugcType) {
        final int topN = getIntegerConfig(ConfigKey.HOT_LIST_TOP_N, 10);
        final PriorityQueue<HotUgcCacheInfo> minHeap = new PriorityQueue<>(
            Comparator.comparingDouble(HotUgcCacheInfo::getHotScore)
        );

        long cursor = System.currentTimeMillis();
        int processedCount = 0;

        while (true) {
            List<UgcDocument> batch = loadUgcList(ugcType, cursor);
            if (batch.isEmpty()) {
                break;
            }

            cursor = batch.get(batch.size() - 1).getGmtModified();
            processedCount += batch.size();
            processBatch(batch, minHeap, topN);

            logger.debug("Processed {} items, current heap size: {}", processedCount, minHeap.size());
        }

        List<HotUgcCacheInfo> topList = extractTopList(minHeap);
        cacheHotList(ugcType, topList);
    }

    private void executeHotJob() {
        if (!jobLock.tryLock()) {
            return;
        }
        try {
            jobLock.lock();
            runWithCost(logger, this::calculateAndCacheHotList, "hotUgcJob");
        } catch (Exception e) {
            logger.error("[UgcHotListJob] Calculate hot list failed", e);
        } finally {
            jobLock.unlock();
        }
    }

    private List<UgcDocument> loadUgcList(UgcType ugcType, long cursor) {
        return ugcRepository.queryByStatusWithTimeCursor(
            ugcType.name(),
            UgcStatus.PUBLISHED.name(),
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    private void processBatch(List<UgcDocument> batch, PriorityQueue<HotUgcCacheInfo> minHeap, int topN) {
        List<HotUgcCacheInfo> cacheInfos = calculateHotScores(batch);
        if (CollectionUtils.isEmpty(cacheInfos)) {
            return;
        }
        for (HotUgcCacheInfo cacheInfo : cacheInfos) {
            if (cacheInfo.getHotScore() <= 0) {
                continue;
            }
            if (minHeap.size() < topN) {
                minHeap.offer(cacheInfo);
                continue;
            }

            HotUgcCacheInfo peek = minHeap.peek();
            if (peek != null && cacheInfo.getHotScore() > peek.getHotScore()) {
                minHeap.poll();
                minHeap.offer(cacheInfo);
            }
        }
    }

    // core logic:
    // score calculation: view_cnt(40%), like_cnt(30%), collect_cnt(20%), commentary_cnt(10%)
    // top 10
    // T+1, get from cache
    private List<HotUgcCacheInfo> calculateHotScores(List<UgcDocument> ugcList) {
        return ugcList.stream().map(ugc -> {
            long commentaryCount = commentaryRepository.queryCountByUgcId(ugc.getUgcId());

            double score = ugc.getViewCount() * 0.4
                + ugc.getLikeCount() * 0.3
                + ugc.getCollectCount() * 0.2
                + commentaryCount * 0.1;

            if (UgcType.POST == UgcType.of(ugc.getType())) {
                return new HotUgcCacheInfo(ugc.getUgcId(), ugc.getContent(), score, ugc.getViewCount());
            }

            return new HotUgcCacheInfo(ugc.getUgcId(), ugc.getTitle(), score, ugc.getViewCount());
        }).collect(Collectors.toList());
    }

    private List<HotUgcCacheInfo> extractTopList(PriorityQueue<HotUgcCacheInfo> minHeap) {
        return minHeap.stream()
            .sorted(Comparator.comparingDouble(HotUgcCacheInfo::getHotScore).reversed())
            .collect(Collectors.toList());
    }

    private void cacheHotList(UgcType ugcType, List<HotUgcCacheInfo> hotList) {
        String cacheKey = ofHotUgcListKey(ugcType.name());
        cacheManager.set(cacheKey, GsonUtil.toJson(hotList), CacheKey.HOT_UGC_LIST.getTtl());
    }
}
