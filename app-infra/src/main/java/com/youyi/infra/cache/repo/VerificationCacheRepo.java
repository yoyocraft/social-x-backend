package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.BizType;
import com.youyi.common.type.cache.CacheKey;
import java.util.Map;

import static com.youyi.common.util.CacheUtil.buildKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
public class VerificationCacheRepo {
    public static String ofEmailCaptchaKey(String email, BizType bizType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "email", email,
            "bizType", bizType.name()
        );
        return buildKey(CacheKey.EMAIL_CAPTCHA, dataMap);
    }
}
