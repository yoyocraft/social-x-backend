package com.youyi.common.type;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
public enum ConfigKey {

    // ================ Email ================
    CAPTCHA_EMAIL_TEMPLATE,
    CAPTCHA_EMAIL_TITLE,
    CAPTCHA_PROCESS_CN_TITLE,
    CAPTCHA_EMAIL_SUBJECT,
    MAIL_FROM,
    PLATFORM_RESPONSIBLE_PERSON,

    // ================ ThreadPool ================
    RECORD_OP_LOG_THREAD_POOL_CONFIG,

    // ================ Feature AB Switch ================
    SEND_EMAIL_AB_SWITCH,
    QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH,

    // ================ Crypto ================
    AES_KEY,
    AES_ALGORITHM,

    // ================ User ================
    DEFAULT_AVATAR,

    // ================ Lock ================
    DEFAULT_LOCAL_LOCK_TIMEOUT,
    ;

}
