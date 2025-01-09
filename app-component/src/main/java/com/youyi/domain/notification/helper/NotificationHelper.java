package com.youyi.domain.notification.helper;

import com.youyi.common.util.RandomGenUtil;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.infra.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Service
@RequiredArgsConstructor
public class NotificationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationHelper.class);

    private final EmailSender emailSender;

    public void notifyCaptcha(NotificationDO notificationDO) {
        // 生成验证码
        String captcha = RandomGenUtil.genEmailCaptcha();
        notificationDO.setCaptcha(captcha);
        LOGGER.info("send captcha:{} to email:{}", captcha, notificationDO.getEmail());
        // 保存验证码
        // TODO youyi 2025/1/9 redis 或者其他存储介质
        // 发送邮件
        // TODO youyi 2025/1/9 支持重试，防抖
        emailSender.sendCaptchaEmail(notificationDO.getEmail(), captcha);
    }
}
