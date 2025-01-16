package com.youyi.common.type;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
public enum OperationType {

    INSERT_CONFIG,
    UPDATE_CONFIG,
    DELETE_CONFIG,

    USER_LOGIN,
    USER_LOGOUT,
    USER_SET_PASSWORD,
    USER_EDIT_INFO,

    NOTIFY_CAPTCHA,
    VERIFY_CAPTCHA,

    ADD_PERMISSION,
    AUTHORIZE_PERMISSION,
    REVOKE_PERMISSION,
    ;
}
