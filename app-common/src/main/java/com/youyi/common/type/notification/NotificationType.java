package com.youyi.common.type.notification;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Getter
public enum NotificationType {

    UNKNOWN,

    CAPTCHA_FOR_LOGIN,

    CAPTCHA_FOR_SET_PASSWORD,

    ;

    public static NotificationType of(String type) {
        return EnumUtils.getEnum(NotificationType.class, type, UNKNOWN);
    }

}
