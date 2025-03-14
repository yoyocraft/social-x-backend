package com.youyi.domain.notification.type;

import com.youyi.common.type.HasCode;
import java.util.Arrays;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Getter
public enum NotificationStatus implements HasCode {

    UNREAD(0),
    READ(1),
    DELETED(Integer.MAX_VALUE),
    ;

    private final Integer code;

    NotificationStatus(Integer code) {
        this.code = code;
    }

    public static NotificationStatus of(Integer code) {
        return Arrays.stream(values())
            .filter(item -> item.getCode().equals(code))
            .findFirst()
            .orElse(DELETED);
    }
}
