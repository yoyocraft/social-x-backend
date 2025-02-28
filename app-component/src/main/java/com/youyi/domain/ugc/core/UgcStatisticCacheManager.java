package com.youyi.domain.ugc.core;

import com.youyi.common.type.cache.CacheKey;
import com.youyi.infra.cache.manager.CacheManager;
import com.youyi.infra.cache.repo.UgcCacheRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class UgcStatisticCacheManager {

    private static final String INCR_OPT = "incr";
    private static final String DECR_OPT = "decr";

    private static final String INCR_WITH_EXPIRE_LUA_SCRIPT = """
        -- Lua 脚本: incr_or_decr_with_expire.lua
        local key = KEYS[1]
        local expire = tonumber(ARGV[1])
        local operation = ARGV[2]  -- operation: "incr" 或 "decr"
                
        if redis.call("EXISTS", key) == 0 then
            redis.call("SET", key, 1, "EX", expire)  -- key 不存在，初始化为 1，并设置过期时间
            return 1
        else
            if operation == "incr" then
                return redis.call("INCR", key)  -- 执行递减
            else
                return redis.call("DECR", key)  -- 执行递增
            end
        end
        """;

    private static final String GET_AND_DEL_LUA_SCRIPT = """
        -- 获取 key 的值并删除 key
        local key = KEYS[1]
        local value = redis.call("GET", key)  -- 获取值
                
        if value then
            redis.call("DEL", key)  -- 删除 key
            return tonumber(value)  -- 转换为数值并返回
        end
                
        return nil  -- 如果 key 不存在，则返回 nil
        """;

    private final CacheManager cacheManager;

    public void incrOrDecrUgcViewCount(String ugcId, boolean incr) {
        String cacheKey = UgcCacheRepo.ofUgcViewCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            INCR_WITH_EXPIRE_LUA_SCRIPT,
            cacheKey,
            CacheKey.UGC_VIEW_COUNT.getTtl().getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcViewCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcViewCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcViewCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcViewCountKey(ugcId);
        return cacheManager.execute(Long.class, GET_AND_DEL_LUA_SCRIPT, cacheKey);
    }

    public void incrOrDecrUgcLikeCount(String ugcId, boolean incr) {
        String cacheKey = UgcCacheRepo.ofUgcLikeCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            INCR_WITH_EXPIRE_LUA_SCRIPT,
            cacheKey,
            CacheKey.UGC_LIKE_COUNT.getTtl().getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcLikeCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcLikeCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcLikeCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcLikeCountKey(ugcId);
        return cacheManager.execute(Long.class, GET_AND_DEL_LUA_SCRIPT, cacheKey);
    }

    public void incrOrDecrUgcCollectCount(String ugcId, boolean incr) {
        String cacheKey = UgcCacheRepo.ofUgcCollectCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            INCR_WITH_EXPIRE_LUA_SCRIPT,
            cacheKey,
            CacheKey.UGC_COLLECT_COUNT.getTtl().getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcCollectCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcCollectCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcCollectCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcCollectCountKey(ugcId);
        return cacheManager.execute(Long.class, GET_AND_DEL_LUA_SCRIPT, cacheKey);
    }

    public void incrOrDecrUgcCommentaryCount(String ugcId, boolean incr) {
        String cacheKey = UgcCacheRepo.ofUgcCommentaryCountKey(ugcId);
        cacheManager.execute(
            Long.class,
            INCR_WITH_EXPIRE_LUA_SCRIPT,
            cacheKey,
            CacheKey.UGC_COLLECT_COUNT.getTtl().getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getUgcCommentaryCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcCommentaryCountKey(ugcId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelUgcCommentaryCount(String ugcId) {
        String cacheKey = UgcCacheRepo.ofUgcCommentaryCountKey(ugcId);
        return cacheManager.execute(Long.class, GET_AND_DEL_LUA_SCRIPT, cacheKey);
    }

    public void incrOrDecrCommentaryLikeCount(String commentaryId, boolean incr) {
        String cacheKey = UgcCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        cacheManager.execute(
            Long.class,
            INCR_WITH_EXPIRE_LUA_SCRIPT,
            cacheKey,
            CacheKey.COMMENTARY_LIKE_COUNT.getTtl().getSeconds(),
            incr ? INCR_OPT : DECR_OPT
        );
    }

    public Long getCommentaryLikeCount(String commentaryId) {
        String cacheKey = UgcCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        Object value = cacheManager.get(cacheKey);
        return Optional.ofNullable(value)
            .map(val -> Long.parseLong(val.toString()))
            .orElse(0L);
    }

    public Long getAndDelCommentaryLikeCount(String commentaryId) {
        String cacheKey = UgcCacheRepo.ofCommentaryLikeCountKey(commentaryId);
        return cacheManager.execute(Long.class, GET_AND_DEL_LUA_SCRIPT, cacheKey);
    }
}
