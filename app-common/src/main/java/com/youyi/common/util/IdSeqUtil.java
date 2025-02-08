package com.youyi.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class IdSeqUtil {

    // ============================= Id Seq ==============================
    private static final long USER_ID_WORKER = 1L;
    private static final Snowflake USER_ID_SEQ = IdUtil.getSnowflake(USER_ID_WORKER);
    private static final long UGC_ID_WORKER = 2L;
    private static final Snowflake UGC_ID_SEQ = IdUtil.getSnowflake(UGC_ID_WORKER);
    private static final long UGC_CATEGORY_ID_WORKER = 3L;
    private static final Snowflake UGC_CATEGORY_ID_SEQ = IdUtil.getSnowflake(UGC_CATEGORY_ID_WORKER);
    private static final long UGC_TAG_ID_WORKER = 4L;
    private static final Snowflake UGC_TAG_ID_SEQ = IdUtil.getSnowflake(UGC_TAG_ID_WORKER);
    private static final long COMMENTARY_ID_WORKER = 5L;
    private static final Snowflake COMMENTARY_ID_SEQ = IdUtil.getSnowflake(COMMENTARY_ID_WORKER);
    private static final long SYS_TASK_ID_WORKER = 6L;
    private static final Snowflake SYS_TASK_ID_SEQ = IdUtil.getSnowflake(SYS_TASK_ID_WORKER);
    private static final long NOTIFICATION_ID_WORKER = 7L;
    private static final Snowflake NOTIFICATION_ID_SEQ = IdUtil.getSnowflake(NOTIFICATION_ID_WORKER);

    // ============================= Constants ==============================
    private static final int EMAIL_CAPTCHA_LENGTH = 6;
    private static final String USER_NICK_NAME_PREFIX = "social_x_";
    private static final int USER_NICK_NAME_SUFFIX_LENGTH = 8;
    private static final int USER_VERIFY_CAPTCHA_TOKEN_LENGTH = 64;
    private static final int USER_PWD_SALT_LENGTH = 64;
    private static final int RESOURCE_KEY_LENGTH = 128;

    private IdSeqUtil() {
    }

    public static String genUserId() {
        return USER_ID_SEQ.nextIdStr();
    }

    public static String genUgcId() {
        return UGC_ID_SEQ.nextIdStr();
    }

    public static String genUgcCategoryId() {
        return UGC_CATEGORY_ID_SEQ.nextIdStr();
    }

    public static String genUgcTagId() {
        return UGC_TAG_ID_SEQ.nextIdStr();
    }

    public static String genCommentaryId() {
        return COMMENTARY_ID_SEQ.nextIdStr();
    }

    public static String genSysTaskId() {
        return SYS_TASK_ID_SEQ.nextIdStr();
    }

    public static String genNotificationId() {
        return NOTIFICATION_ID_SEQ.nextIdStr();
    }

    public static String genEmailCaptcha() {
        return RandomStringUtils.secure().nextNumeric(EMAIL_CAPTCHA_LENGTH);
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
}
