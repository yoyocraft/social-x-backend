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

    public static final String USER_VERIFY_TOKEN_KEY = ofKey("user", "verify", "captcha", "${email}", "${bizType}");
    public static final Duration USER_VERIFY_TOKEN_TTL = Duration.ofMinutes(10);

    public static String ofUserVerifyTokenKey(String email, BizType bizType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "email", email,
            "bizType", bizType.name()
        );
        return buildKey(USER_VERIFY_TOKEN_KEY, dataMap);
    }
}
