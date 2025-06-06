package com.youyi.infra.cache.key;

import com.google.common.collect.ImmutableMap;
import com.youyi.infra.cache.CacheKey;
import java.util.Map;

import static com.youyi.infra.cache.CacheUtil.buildKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public class UgcCacheKeyRepo {

    public static String ofUgcViewCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(CacheKey.UGC_VIEW_COUNT, dataMap);
    }

    public static String ofUgcLikeCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(CacheKey.UGC_LIKE_COUNT, dataMap);
    }

    public static String ofUgcCollectCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(CacheKey.UGC_COLLECT_COUNT, dataMap);
    }

    public static String ofUgcCommentaryCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(CacheKey.UGC_COMMENTARY_COUNT, dataMap);
    }

    public static String ofCommentaryLikeCountKey(String commentaryId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "commentaryId", commentaryId
        );
        return buildKey(CacheKey.COMMENTARY_LIKE_COUNT, dataMap);
    }

    public static String ofUgcUserRecommendTagKey(String userId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "userId", userId
        );
        return buildKey(CacheKey.UGC_USER_RECOMMEND_TAG, dataMap);
    }

    public static String ofHotUgcListKey(String ugcType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcType", ugcType
        );
        return buildKey(CacheKey.HOT_UGC_LIST, dataMap);
    }
}
