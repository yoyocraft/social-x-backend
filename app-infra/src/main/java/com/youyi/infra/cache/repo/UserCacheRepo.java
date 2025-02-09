package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.BizType;
import java.time.Duration;
import java.util.Map;

import static com.youyi.infra.cache.util.CacheUtil.buildKey;
import static com.youyi.infra.cache.util.CacheUtil.ofKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
public class UserCacheRepo {

    /**
     * u:vrf:cap:${email}:${bizType}
     */
    public static final String USER_VERIFY_TOKEN_KEY = ofKey("u", "vrf", "cap", "${email}", "${bizType}");
    public static final Duration USER_VERIFY_TOKEN_TTL = Duration.ofMinutes(10);

    /**
     * u:fl:${userId}
     * 常驻缓存
     */
    public static final String USER_FOLLOW_IDS_KEY = ofKey("u", "fl", "${userId}");

    public static String ofUserVerifyTokenKey(String email, BizType bizType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "email", email,
            "bizType", bizType.name()
        );
        return buildKey(USER_VERIFY_TOKEN_KEY, dataMap);
    }

    public static String ofUserFollowIdsKey(String userId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "userId", userId
        );
        return buildKey(USER_FOLLOW_IDS_KEY, dataMap);
    }
}
