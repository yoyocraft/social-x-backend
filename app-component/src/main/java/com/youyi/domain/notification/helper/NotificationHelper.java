package com.youyi.domain.notification.helper;

import com.youyi.common.util.RandomGenUtil;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.infra.cache.manager.CacheManager;
import com.youyi.infra.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.youyi.infra.cache.repo.NotificationCacheRepo.EMAIL_CAPTCHA_DURATION;
import static com.youyi.infra.cache.repo.NotificationCacheRepo.ofEmailCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Service
@RequiredArgsConstructor
public class NotificationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationHelper.class);

    private final EmailSender emailSender;
    private final CacheManager cacheManager;

    public void notifyCaptcha(NotificationDO notificationDO) {
        // 生成验证码
        String captcha = RandomGenUtil.genEmailCaptcha();
        notificationDO.setCaptcha(captcha);
        // 保存验证码
        saveCaptcha(notificationDO);
        // 发送邮件
        // TODO youyi 2025/1/9 支持重试，防抖，异步发送即可
        emailSender.sendCaptchaEmail(notificationDO.getEmail(), captcha);
    }

    private void saveCaptcha(NotificationDO notificationDO) {
        String cacheKey = ofEmailCaptchaKey(notificationDO.getEmail(), notificationDO.getNotificationType());
        cacheManager.set(cacheKey, notificationDO.getCaptcha(), EMAIL_CAPTCHA_DURATION);
    }
}
