package com.youyi.domain.ugc.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@AllArgsConstructor
public enum UgcTagType {

    FOR_ARTICLE(0),
    FOR_USER_INTEREST(1),
    ;

    private final int type;

    public static UgcTagType of(int type) {
        return Arrays.stream(UgcTagType.values())
            .filter(item -> item.getType() == type)
            .findFirst()
            .orElse(FOR_ARTICLE);
    }
}
