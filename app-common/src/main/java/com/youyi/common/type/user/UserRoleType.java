package com.youyi.common.type.user;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
public enum UserRoleType {

    USER,
    ADMIN,
    ;

    public static UserRoleType of(String type) {
        return EnumUtils.getEnum(UserRoleType.class, type, USER);
    }
}
