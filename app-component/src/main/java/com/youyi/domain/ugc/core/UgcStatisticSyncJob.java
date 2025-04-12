package com.youyi.domain.ugc.core;

import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.infra.conf.core.ConfigKey;
import com.youyi.infra.tpe.TpeContainer;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;
import static com.youyi.infra.conf.util.CommonConfUtil.hasScheduleOn;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class UgcStatisticSyncJob implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UgcStatisticSyncJob.class);

    private static final long SYNC_INTERVAL = 60 * 60 * 1000L;
    private static final long SYNC_INIT_DELAY_INTERVAL = 5 * 60 * 1000L;

    private final TpeContainer tpeContainer;

    private final UgcRepository ugcRepository;
    private final CommentaryRepository commentaryRepository;

    private final UgcStatisticCacheManager ugcStatisticCacheManager;

    @Scheduled(initialDelay = SYNC_INIT_DELAY_INTERVAL, fixedDelay = SYNC_INTERVAL)
    public void syncJob() {
        execSyncJob();
    }

    /**
     * @see org.springframework.context.support.AbstractApplicationContext#close()
     */
    @Override
    public void onApplicationEvent(@Nonnull ContextClosedEvent event) {
        execSyncJob();
    }

    private void execSyncJob() {
        if (!hasScheduleOn()) {
            return;
        }
        try {
            runWithCost(logger, this::syncUgcStatistic, "syncUgcStatistic");
            runWithCost(logger, this::syncCommentaryStatistic, "syncCommentaryStatistic");
        } catch (Exception e) {
            logger.error("[UgcStatisticSyncJob] sync exp", e);
        }
    }

    private void syncCommentaryStatistic() {
        long cursor = System.currentTimeMillis();
        while (true) {
            List<CommentaryDocument> commentaryList = loadCommentaryList(cursor);
            if (commentaryList.isEmpty()) {
                break;
            }
            commentaryList.forEach(commentaryDocument -> tpeContainer.getUgcStatisticsExecutor().execute(() -> doSyncCommentaryStatistic(commentaryDocument)));
            cursor = commentaryList.get(commentaryList.size() - 1).getGmtModified();
        }
    }

    private void syncUgcStatistic() {
        long cursor = System.currentTimeMillis();
        while (true) {
            List<UgcDocument> ugcList = loadUgcList(cursor);
            if (ugcList.isEmpty()) {
                break;
            }
            ugcList.forEach(ugcDocument -> tpeContainer.getUgcStatisticsExecutor().execute(() -> doSyncUgcStatistic(ugcDocument)));
            cursor = ugcList.get(ugcList.size() - 1).getGmtModified();
        }
    }

    private void doSyncUgcStatistic(UgcDocument ugcDocument) {
        String ugcId = ugcDocument.getUgcId();
        Long toAddViewCount = Optional.ofNullable(ugcStatisticCacheManager.getAndDelUgcViewCount(ugcId)).orElse(0L);
        Long toAddLikeCount = Optional.ofNullable(ugcStatisticCacheManager.getAndDelUgcLikeCount(ugcId)).orElse(0L);
        Long toAddCollectCount = Optional.ofNullable(ugcStatisticCacheManager.getAndDelUgcCollectCount(ugcId)).orElse(0L);
        Long toAddCommentaryCount = Optional.ofNullable(ugcStatisticCacheManager.getAndDelUgcCommentaryCount(ugcId)).orElse(0L);
        ugcRepository.incrUgcStatisticCount(ugcId, toAddViewCount, toAddLikeCount, toAddCollectCount, toAddCommentaryCount);
    }

    private void doSyncCommentaryStatistic(CommentaryDocument commentaryDocument) {
        String commentaryId = commentaryDocument.getCommentaryId();
        Long toAddLikeCount = Optional.ofNullable(ugcStatisticCacheManager.getAndDelCommentaryLikeCount(commentaryId)).orElse(0L);
        commentaryRepository.incrLikeCount(commentaryId, toAddLikeCount);
    }

    private List<UgcDocument> loadUgcList(long cursor) {
        return ugcRepository.queryByStatusWithTimeCursor(
            UgcType.ALL.name(),
            UgcStatus.PUBLISHED.name(),
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    private List<CommentaryDocument> loadCommentaryList(long cursor) {
        return commentaryRepository.queryWithTimeCursor(
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

}
