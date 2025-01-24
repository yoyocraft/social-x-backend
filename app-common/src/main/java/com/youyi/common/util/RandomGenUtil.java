package com.youyi.common.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class RandomGenUtil {

    // ============================= Constants ==============================
    public static final int EMAIL_CAPTCHA_LENGTH = 6;

    public static final int USER_ID_LENGTH = 32;

    public static final String USER_NICK_NAME_PREFIX = "social_x_";
    public static final int USER_NICK_NAME_SUFFIX_LENGTH = 8;

    public static final int USER_VERIFY_CAPTCHA_TOKEN_LENGTH = 64;

    public static final int USER_PWD_SALT_LENGTH = 64;

    public static final int RESOURCE_KEY_LENGTH = 128;

    public static final int UGC_ID_LENGTH = 32;

    private RandomGenUtil() {
    }

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

    public static String genResourceKey() {
        return RandomStringUtils.secure().nextAlphanumeric(RESOURCE_KEY_LENGTH);
    }

    public static String genUgcId() {
        return RandomStringUtils.secure().nextNumeric(UGC_ID_LENGTH);
    }
}
