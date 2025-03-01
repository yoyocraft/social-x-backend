package com.youyi.domain.ugc.core;

import com.youyi.common.type.cache.CacheKey;
import com.youyi.common.type.ugc.UgcStatisticType;
import com.youyi.infra.cache.manager.CacheManager;
import com.youyi.infra.cache.repo.UgcCacheRepo;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class UgcStatisticCacheManager {

    private static final EnumMap<UgcStatisticType, Function<String, String>> statisticFunc = new EnumMap<>(UgcStatisticType.class);

    static {
        statisticFunc.put(UgcStatisticType.VIEW, UgcCacheRepo::ofUgcViewCountKey);
        statisticFunc.put(UgcStatisticType.LIKE, UgcCacheRepo::ofUgcLikeCountKey);
        statisticFunc.put(UgcStatisticType.COLLECT, UgcCacheRepo::ofUgcCollectCountKey);
        statisticFunc.put(UgcStatisticType.COMMENTARY, UgcCacheRepo::ofUgcCommentaryCountKey);
    }

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

    public EnumMap<UgcStatisticType, Map<String, Long>> getBatchUgcStatistic(List<String> ugcIds, List<UgcStatisticType> types) {
        if (CollectionUtils.isEmpty(ugcIds) || CollectionUtils.isEmpty(types)) {
            return new EnumMap<>(UgcStatisticType.class);
        }

        EnumMap<UgcStatisticType, List<String>> keysMap = new EnumMap<>(UgcStatisticType.class);
        for (UgcStatisticType type : types) {
            List<String> keys = ugcIds.stream().map(statisticFunc.get(type)).toList();
            keysMap.put(type, keys);
        }

        // 一次性查询
        List<String> allKeys = keysMap.values().stream().flatMap(List::stream).toList();
        List<Object> results = cacheManager.getPipelineResult(allKeys);

        // 解析结果
        EnumMap<UgcStatisticType, Map<String, Long>> statisticsMap = new EnumMap<>(UgcStatisticType.class);
        int index = 0;
        for (UgcStatisticType type : types) {
            Map<String, Long> typeStatisticMap = new HashMap<>();
            List<String> keys = keysMap.get(type);
            for (String key : keys) {
                Object value = results.get(index++);
                typeStatisticMap.put(key, value == null ? 0L : Long.parseLong(value.toString()));
            }
            statisticsMap.put(type, typeStatisticMap);
        }

        return statisticsMap;
    }
}
