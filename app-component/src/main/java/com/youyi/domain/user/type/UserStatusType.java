package com.youyi.domain.user.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@AllArgsConstructor
public enum UserStatusType {

    NORMAL(0),
    LOCKED(1),
    DELETED(2),
    ;

    private final int value;

    public static UserStatusType of(int value) {
        return Arrays.stream(UserStatusType.values())
            .filter(genderType -> genderType.value == value)
            .findFirst()
            .orElse(NORMAL);
    }
}
