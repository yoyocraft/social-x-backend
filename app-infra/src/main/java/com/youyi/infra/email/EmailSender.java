package com.youyi.infra.email;

import com.youyi.common.exception.AppSystemException;
import com.youyi.infra.config.core.ConfigCacheService;
import java.text.MessageFormat;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.ConfigKey.CAPTCHA_EMAIL_SUBJECT;
import static com.youyi.common.type.ConfigKey.CAPTCHA_EMAIL_TEMPLATE;
import static com.youyi.common.type.ConfigKey.CAPTCHA_EMAIL_TITLE;
import static com.youyi.common.type.ConfigKey.CAPTCHA_PROCESS_CN_TITLE;
import static com.youyi.common.type.ConfigKey.MAIL_FROM;
import static com.youyi.common.type.ConfigKey.PLATFORM_RESPONSIBLE_PERSON;
import static com.youyi.common.type.ConfigKey.SEND_EMAIL_AB_SWITCH;
import static com.youyi.common.type.InfraCode.SEND_EMAIL_ERROR;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    private final ConfigCacheService configCacheService;
    private final JavaMailSender mailSender;

    private static final String MAIL_FROM_FORMATTER = "%s<%s>";

    public void sendCaptchaEmail(String emailTo, String captcha) {
        boolean canSend = configCacheService.getBooleanConfig(SEND_EMAIL_AB_SWITCH);
        if (!canSend) {
            LOGGER.info("[EmailSender] send captcha:{} to email:{}", captcha, emailTo);
            return;
        }
        doSendCaptchaEmail(emailTo, captcha);
    }

    private void doSendCaptchaEmail(String emailTo, String captcha) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setSubject(configCacheService.getStringConfig(CAPTCHA_EMAIL_SUBJECT));
            messageHelper.setText(buildCaptchaEmailContent(captcha), true);
            messageHelper.setTo(emailTo);
            String from = String.format(MAIL_FROM_FORMATTER, configCacheService.getStringConfig(CAPTCHA_PROCESS_CN_TITLE), configCacheService.getStringConfig(MAIL_FROM));
            messageHelper.setFrom(from);
            mailSender.send(message);
        } catch (Exception e) {
            throw AppSystemException.of(SEND_EMAIL_ERROR, e);
        }
    }

    private String buildCaptchaEmailContent(String captcha) {
        String emailTemplate = configCacheService.getStringConfig(CAPTCHA_EMAIL_TEMPLATE);
        String emailTitle = configCacheService.getStringConfig(CAPTCHA_EMAIL_TITLE);
        String processCnTitle = configCacheService.getStringConfig(CAPTCHA_PROCESS_CN_TITLE);
        String platformResponsiblePerson = configCacheService.getStringConfig(PLATFORM_RESPONSIBLE_PERSON);
        return MessageFormat.format(emailTemplate, captcha, emailTitle, processCnTitle, platformResponsiblePerson);
    }

}
