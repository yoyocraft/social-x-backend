package com.youyi.common.type.notification;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/01
 */
@Getter
public enum NotificationType {

    UNKNOWN,
    ALL,
    COMMENT,
    INTERACT,
    FOLLOW,
    SYSTEM,
    ;

    public static NotificationType of(String type) {
        return EnumUtils.getEnum(NotificationType.class, type, UNKNOWN);
    }
}
