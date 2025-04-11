package com.youyi.domain.user.type;

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
    MOBILE(4),
    UI_UX(5),
    PRODUCT(6),
    TEST(7),
    OPERATION(8),
    DATA_ANALYSIS(9),
    ARTIFICIAL_INTELLIGENCE(10),
    OTHER(11),
    ;

    private final Integer code;

    public static WorkDirectionType fromCode(Integer code) {
        return Arrays.stream(WorkDirectionType.values())
            .filter(item -> item.getCode().equals(code))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
