package com.youyi.infra.cache.repo;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.notification.NotificationType;
import java.time.Duration;
import java.util.Map;

import static com.youyi.infra.cache.util.CacheUtil.buildKey;
import static com.youyi.infra.cache.util.CacheUtil.ofKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
public class NotificationCacheRepo {

    public static final String EMAIL_CAPTCHA_KEY = ofKey("email", "captcha", "${email}", "${notificationType}");
    public static final Duration EMAIL_CAPTCHA_DURATION = Duration.ofMinutes(10);

    public static String ofEmailCaptchaKey(String email, NotificationType notificationType) {
        Map<String, String> dataMap = ImmutableMap.of(
            "email", email,
            "notificationType", notificationType.name()
        );
        return buildKey(EMAIL_CAPTCHA_KEY, dataMap);
    }
}
