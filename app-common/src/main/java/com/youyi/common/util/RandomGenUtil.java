package com.youyi.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import static com.youyi.common.constant.RandGenConstant.EMAIL_CAPTCHA_LENGTH;
import static com.youyi.common.constant.RandGenConstant.USER_ID_LENGTH;
import static com.youyi.common.constant.RandGenConstant.USER_NICK_NAME_PREFIX;
import static com.youyi.common.constant.RandGenConstant.USER_NICK_NAME_SUFFIX_LENGTH;
import static com.youyi.common.constant.RandGenConstant.USER_PWD_SALT_LENGTH;
import static com.youyi.common.constant.RandGenConstant.USER_VERIFY_CAPTCHA_TOKEN_LENGTH;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class RandomGenUtil {

    public static String genEmailCaptcha() {
        return RandomStringUtils.secure().nextNumeric(EMAIL_CAPTCHA_LENGTH);
    }

    public static String genUserId() {
        return RandomStringUtils.secure().nextNumeric(USER_ID_LENGTH);
    }

    public static String genUserNickName() {
        return USER_NICK_NAME_PREFIX + RandomStringUtils.secure().nextNumeric(USER_NICK_NAME_SUFFIX_LENGTH);
    }

    public static String genUserVerifyCaptchaToken() {
        return RandomStringUtils.secure().nextAlphanumeric(USER_VERIFY_CAPTCHA_TOKEN_LENGTH);
    }

    public static String genPwdSalt() {
        return RandomStringUtils.secure().nextAlphanumeric(USER_PWD_SALT_LENGTH);
    }
}
