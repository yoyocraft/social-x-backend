package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Map;

import static com.youyi.infra.cache.util.CacheUtil.buildKey;
import static com.youyi.infra.cache.util.CacheUtil.ofKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public class StatisticCacheRepo {

    public static final String UGC_VIEW_COUNT_KEY = ofKey("ugc", "${ugcId}", "sv");
    public static final Duration UGC_VIEW_COUNT_TTL = Duration.ofHours(24);

    public static final String UGC_LIKE_COUNT_KEY = ofKey("ugc", "${ugcId}", "sl");
    public static final Duration UGC_LIKE_COUNT_TTL = Duration.ofHours(24);

    public static final String UGC_COLLECT_COUNT_KEY = ofKey("ugc", "${ugcId}", "sc");
    public static final Duration UGC_COLLECT_COUNT_TTL = Duration.ofHours(24);

    public static final String COMMENTARY_LIKE_COUNT_KEY = ofKey("cmt", "${commentaryId}", "cl");
    public static final Duration COMMENTARY_LIKE_COUNT_TTL = Duration.ofHours(24);

    public static String ofUgcViewCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(UGC_VIEW_COUNT_KEY, dataMap);
    }

    public static String ofUgcLikeCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(UGC_LIKE_COUNT_KEY, dataMap);
    }

    public static String ofUgcCollectCountKey(String ugcId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "ugcId", ugcId
        );
        return buildKey(UGC_COLLECT_COUNT_KEY, dataMap);
    }

    public static String ofCommentaryLikeCountKey(String commentaryId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "commentaryId", commentaryId
        );
        return buildKey(COMMENTARY_LIKE_COUNT_KEY, dataMap);
    }

}
