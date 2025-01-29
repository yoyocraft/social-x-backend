package com.youyi.domain.ugc.cache;

import com.youyi.infra.cache.manager.CacheManager;
import com.youyi.infra.cache.repo.StatisticCacheRepo;
import java.util.Optional;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.conf.ConfigKey.GET_AND_DEL_LUA_SCRIPT;
import static com.youyi.common.type.conf.ConfigKey.INCR_WITH_EXPIRE_LUA_SCRIPT;
import static com.youyi.infra.cache.repo.StatisticCacheRepo.COMMENTARY_LIKE_COUNT_TTL;
import static com.youyi.infra.cache.repo.StatisticCacheRepo.UGC_COLLECT_COUNT_TTL;
import static com.youyi.infra.cache.repo.StatisticCacheRepo.UGC_LIKE_COUNT_TTL;
import static com.youyi.infra.cache.repo.StatisticCacheRepo.UGC_VIEW_COUNT_TTL;
import static com.youyi.infra.conf.core.SystemConfigService.checkConfig;
import static com.youyi.infra.conf.core.SystemConfigService.getStringConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class UgcStatisticCacheManager implements ApplicationListener<ApplicationReadyEvent> {

    private static final String INCR_OPT = "incr";
    private static final String DECR_OPT = "decr";

    private final CacheManager cacheManager;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(INCR_WITH_EXPIRE_LUA_SCRIPT);
        checkConfig(GET_AND_DEL_LUA_SCRIPT);
    }

    public void incrOrDecrUgcViewCount(String ugcId, boolean incr) {
        String cacheKey = StatisticCacheRepo.ofUgcViewCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            getStringConfig(INCR_WITH_EXPIRE_LUA_SCRIPT),
            cacheKey,
            UGC_VIEW_COUNT_TTL.getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcViewCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcViewCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcViewCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcViewCountKey(ugcId);
        return cacheManager.execute(
            Long.class,
            getStringConfig(GET_AND_DEL_LUA_SCRIPT),
            cacheKey
        );
    }

    public void incrOrDecrUgcLikeCount(String ugcId, boolean incr) {
        String cacheKey = StatisticCacheRepo.ofUgcLikeCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            getStringConfig(INCR_WITH_EXPIRE_LUA_SCRIPT),
            cacheKey,
            UGC_LIKE_COUNT_TTL.getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcLikeCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcLikeCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcLikeCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcLikeCountKey(ugcId);
        return cacheManager.execute(
            Long.class,
            getStringConfig(GET_AND_DEL_LUA_SCRIPT),
            cacheKey
        );
    }

    public void incrOrDecrUgcCollectCount(String ugcId, boolean incr) {
        String cacheKey = StatisticCacheRepo.ofUgcCollectCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            getStringConfig(INCR_WITH_EXPIRE_LUA_SCRIPT),
            cacheKey,
            UGC_COLLECT_COUNT_TTL.getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcCollectCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcCollectCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcCollectCount(String ugcId) {
        String cacheKey = StatisticCacheRepo.ofUgcCollectCountKey(ugcId);
        return cacheManager.execute(
            Long.class,
            getStringConfig(GET_AND_DEL_LUA_SCRIPT),
            cacheKey
        );
    }

    public void incrOrDecrCommentaryLikeCount(String commentaryId, boolean incr) {
        String cacheKey = StatisticCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        cacheManager.execute(
            Long.class,
            getStringConfig(INCR_WITH_EXPIRE_LUA_SCRIPT),
            cacheKey,
            COMMENTARY_LIKE_COUNT_TTL.getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getCommentaryLikeCount(String commentaryId) {
        String cacheKey = StatisticCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelCommentaryLikeCount(String commentaryId) {
        String cacheKey = StatisticCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        return cacheManager.execute(
            Long.class,
            getStringConfig(GET_AND_DEL_LUA_SCRIPT),
            cacheKey
        );
    }

}
