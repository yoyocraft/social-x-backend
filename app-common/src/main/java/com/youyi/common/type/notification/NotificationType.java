package com.youyi.common.type.notification;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
public enum NotificationType {

    UNKNOWN,
    /**
     * for sum count
     */
    ALL,
    USER_FOLLOW,

    UGC_LIKE,
    UGC_COMMENT,
    UGC_COMMENT_REPLY,
    ;

    public static NotificationType of(String type) {
        return EnumUtils.getEnum(NotificationType.class, type, UNKNOWN);
    }
}
