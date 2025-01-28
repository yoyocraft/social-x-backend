package com.youyi.common.type.user;

import com.youyi.common.type.HasCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
@Getter
@AllArgsConstructor
public enum WorkDirectionType implements HasCode {
    UNKNOWN(0),

    BACKEND(1),

    FRONTEND(2),

    FULLSTACK(3),
    ;

    private final Integer code;

    public static WorkDirectionType fromCode(Integer code) {
        return Arrays.stream(WorkDirectionType.values())
            .filter(item -> item.getCode().equals(code))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
