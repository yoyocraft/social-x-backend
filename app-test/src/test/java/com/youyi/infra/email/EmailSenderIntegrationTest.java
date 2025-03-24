package com.youyi.infra.email;

import com.youyi.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
class EmailSenderIntegrationTest extends BaseIntegrationTest {

    @Autowired
    EmailSender emailSender;

    @Test
    void testSendCaptchaEmail() {
        String captcha = "123456";
        emailSender.sendCaptchaEmail("codejuzi@163.com", captcha);
    }

}
