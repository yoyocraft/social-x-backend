package com.youyi.common.type;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
public enum OperationType {

    UNKNOWN,

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

    UGC_PUBLISH,
    ;

    public static OperationType of(String type) {
        return EnumUtils.getEnum(OperationType.class, type, UNKNOWN);
    }
}
