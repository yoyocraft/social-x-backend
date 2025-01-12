package com.youyi.common.util;

import com.youyi.common.constant.RandGenConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static com.youyi.common.constant.RandGenConstant.EMAIL_CAPTCHA_LENGTH;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
class RandomGenUtilTest {

    @Test
    void testGenEmailCaptcha() {
        String result = RandomGenUtil.genEmailCaptcha();
        Assertions.assertTrue(StringUtils.isNotBlank(result) && result.length() == EMAIL_CAPTCHA_LENGTH);
    }

    @Test
    void testGenUserVerifyCaptchaToken() {
        String result = RandomGenUtil.genUserVerifyCaptchaToken();
        Assertions.assertTrue(StringUtils.isNotBlank(result) && result.length() == RandGenConstant.USER_VERIFY_CAPTCHA_TOKEN_LENGTH);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme