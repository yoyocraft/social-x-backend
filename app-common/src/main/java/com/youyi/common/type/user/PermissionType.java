package com.youyi.common.type.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Getter
@AllArgsConstructor
public enum PermissionType {

    // =========================== System Config ===========================
    CONFIG_MANAGER("CONFIG_MANAGER"),
    CREATE_CONFIG("CREATE_CONFIG"),
    READ_CONFIG("READ_CONFIG"),
    UPDATE_CONFIG("UPDATE_CONFIG"),
    DELETE_CONFIG("DELETE_CONFIG"),

    // =========================== System Permission ===========================
    PERMISSION_MANAGER("PERMISSION_MANAGER"),
    ADD_PERMISSION("ADD_PERMISSION"),
    AUTHORIZE_PERMISSION("AUTHORIZE_PERMISSION"),
    REVOKE_PERMISSION("REVOKE_PERMISSION"),
    ;
    private final String value;

    public static PermissionType of(String permission) {
        if (StringUtils.isBlank(permission)) {
            throw new IllegalArgumentException("permission cannot be null or empty");
        }
        return PermissionType.valueOf(permission.toUpperCase());
    }

}
