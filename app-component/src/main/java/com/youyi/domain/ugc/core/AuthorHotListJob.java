package com.youyi.domain.ugc.core;

import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.HotAuthorCacheInfo;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.infra.cache.CacheKey;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.infra.cache.key.UserCacheKeyRepo.ofHotAuthorListKey;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;
import static com.youyi.infra.conf.util.CommonConfUtil.hasScheduleOn;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/06
 */
@Component
@RequiredArgsConstructor
public class AuthorHotListJob implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorHotListJob.class);

    private final UgcRepository ugcRepository;
    private final CacheRepository cacheRepository;

    private final ReentrantLock jobLock = new ReentrantLock();
    private static final long HOT_AUTHOR_JOB_INTERVAL = 3L * 60 * 60 * 1000;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        executeHotAuthorJob();
    }

    @Scheduled(initialDelay = HOT_AUTHOR_JOB_INTERVAL, fixedDelay = HOT_AUTHOR_JOB_INTERVAL)
    public void hotAuthorJob() {
        executeHotAuthorJob();
    }

    private void executeHotAuthorJob() {
        if (!hasScheduleOn()) {
            return;
        }
        if (!jobLock.tryLock()) {
            return;
        }
        try {
            jobLock.lock();
            runWithCost(logger, this::calculateAndCacheHotAuthors, "hotAuthorJob");
        } catch (Exception e) {
            logger.error("[AuthorHotListJob] Calculate hot authors failed", e);
        } finally {
            jobLock.unlock();
        }
    }

    private void calculateAndCacheHotAuthors() {
        final int topN = getIntegerConfig(ConfigKey.HOT_LIST_TOP_N, 10);
        final Map<String, Double> authorScoreMap = new HashMap<>();

        long cursor = System.currentTimeMillis();
        int processedCount = 0;

        while (true) {
            List<UgcDocument> batch = loadUgcList(cursor);
            if (batch.isEmpty()) {
                break;
            }

            cursor = batch.get(batch.size() - 1).getGmtModified();
            processedCount += batch.size();
            processBatch(batch, authorScoreMap);

            logger.debug("Processed {} items, current author count: {}", processedCount, authorScoreMap.size());
        }

        List<HotAuthorCacheInfo> topAuthors = extractTopAuthors(authorScoreMap, topN);
        cacheHotAuthors(topAuthors);
    }

    private List<UgcDocument> loadUgcList(long cursor) {
        return ugcRepository.queryByStatusWithTimeCursor(
            null,
            UgcStatus.PUBLISHED.name(),
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    private void processBatch(List<UgcDocument> batch, Map<String, Double> authorScoreMap) {
        for (UgcDocument ugc : batch) {
            String authorId = ugc.getAuthorId();

            double score = ugc.getViewCount() * 0.4
                + ugc.getLikeCount() * 0.3
                + ugc.getCollectCount() * 0.2
                + ugc.getCommentaryCount() * 0.1;

            authorScoreMap.put(authorId, authorScoreMap.getOrDefault(authorId, 0.0) + score);
        }
    }

    private List<HotAuthorCacheInfo> extractTopAuthors(Map<String, Double> authorScoreMap, int topN) {
        return authorScoreMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(topN)
            .map(entry -> new HotAuthorCacheInfo(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private void cacheHotAuthors(List<HotAuthorCacheInfo> hotAuthors) {
        String cacheKey = ofHotAuthorListKey();
        cacheRepository.set(cacheKey, GsonUtil.toJson(hotAuthors), CacheKey.HOT_AUTHOR_LIST.getTtl());
    }
}
