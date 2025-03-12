package com.youyi.common.type.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Getter
@AllArgsConstructor
public enum PermissionType {

    UNKNOWN,

    // =========================== System Config ===========================
    CONFIG_MANAGER,
    CREATE_CONFIG,
    READ_CONFIG,
    UPDATE_CONFIG,
    DELETE_CONFIG,

    // =========================== System Permission ===========================
    PERMISSION_MANAGER,
    ADD_PERMISSION,
    AUTHORIZE_PERMISSION,
    REVOKE_PERMISSION,

    // =========================== System Notification ===========================
    NOTIFICATION_MANAGER,
    ;

    public static PermissionType of(String permission) {
        return EnumUtils.getEnum(PermissionType.class, permission, UNKNOWN);
    }

}
