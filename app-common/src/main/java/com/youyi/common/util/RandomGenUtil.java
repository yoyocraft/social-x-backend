package com.youyi.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import static com.youyi.common.constant.CodeGenConstant.EMAIL_CAPTCHA_LENGTH;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class RandomGenUtil {

    public static String genEmailCaptcha() {
        return RandomStringUtils.secure().nextNumeric(EMAIL_CAPTCHA_LENGTH);
    }
}
