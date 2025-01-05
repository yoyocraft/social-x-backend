package com.youyi.infra.email;

import com.youyi.common.exception.AppSystemException;
import com.youyi.infra.config.core.ConfigCacheService;
import java.text.MessageFormat;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.ConfigName.CAPTCHA_EMAIL_TEMPLATE;
import static com.youyi.common.type.ConfigName.EMAIL_SUBJECT;
import static com.youyi.common.type.ConfigName.EMAIL_TITLE;
import static com.youyi.common.type.ConfigName.MAIL_FROM;
import static com.youyi.common.type.ConfigName.PLATFORM_RESPONSIBLE_PERSON;
import static com.youyi.common.type.ConfigName.PROCESS_CN_TITLE;
import static com.youyi.common.type.InfraCode.SEND_EMAIL_ERROR;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final ConfigCacheService configCacheService;
    private final JavaMailSender mailSender;

    private static final String MAIL_FROM_FORMATTER = "%s<%s>";

    public void sendCaptchaEmail(String emailTo, String captcha) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setSubject(configCacheService.getStringConfig(EMAIL_SUBJECT));
            messageHelper.setText(buildCaptchaEmailContent(captcha), true);
            messageHelper.setTo(emailTo);
            String from = String.format(MAIL_FROM_FORMATTER, configCacheService.getStringConfig(PROCESS_CN_TITLE), configCacheService.getStringConfig(MAIL_FROM));
            messageHelper.setFrom(from);
            mailSender.send(message);
        } catch (Exception e) {
            throw AppSystemException.of(SEND_EMAIL_ERROR, e);
        }
    }

    private String buildCaptchaEmailContent(String captcha) {
        String emailTemplate = configCacheService.getStringConfig(CAPTCHA_EMAIL_TEMPLATE);
        String emailTitle = configCacheService.getStringConfig(EMAIL_TITLE);
        String processCnTitle = configCacheService.getStringConfig(PROCESS_CN_TITLE);
        String platformResponsiblePerson = configCacheService.getStringConfig(PLATFORM_RESPONSIBLE_PERSON);
        return MessageFormat.format(emailTemplate, captcha, emailTitle, processCnTitle, platformResponsiblePerson);
    }

}
