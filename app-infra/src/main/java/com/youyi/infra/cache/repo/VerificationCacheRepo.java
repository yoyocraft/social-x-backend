package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.BizType;
import com.youyi.infra.cache.CacheKey;
import java.util.Map;

import static com.youyi.infra.cache.CacheUtil.buildKey;

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

    public static String ofImageCaptchaKey(String captchaId) {
        Map<String, String> dataMap = ImmutableMap.of(
            "captchaId", captchaId
        );
        return buildKey(CacheKey.IMAGE_CAPTCHA, dataMap);
    }
}
