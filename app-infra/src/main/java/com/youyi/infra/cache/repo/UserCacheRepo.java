package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.BizType;
import com.youyi.infra.cache.CacheKey;
import java.util.Map;

import static com.youyi.infra.cache.CacheUtil.buildKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
public class UserCacheRepo {

    public static String ofUserVerifyTokenKey(String email, BizType bizType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "email", email,
            "bizType", bizType.name()
        );
        return buildKey(CacheKey.USER_VERIFY_TOKEN, dataMap);
    }

    public static String ofUserFollowIdsKey(String userId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "userId", userId
        );
        return buildKey(CacheKey.USER_FOLLOW_IDS, dataMap);
    }

    public static String ofHotAuthorListKey() {
        return buildKey(CacheKey.HOT_AUTHOR_LIST, null);
    }

}
