package com.youyi.common.type.user;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@AllArgsConstructor
public enum GenderType {

    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    private final int value;

    public static GenderType of(int value) {
        return Arrays.stream(GenderType.values())
            .filter(genderType -> genderType.value == value)
            .findFirst()
            .orElse(UNKNOWN);
    }
}
