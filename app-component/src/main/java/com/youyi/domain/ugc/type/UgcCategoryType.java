package com.youyi.domain.ugc.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/27
 */
@Getter
@AllArgsConstructor
public enum UgcCategoryType {

    ARTICLE_CATEGORY(0),
    POST_TOPIC(1),
    QUESTION_CATEGORY(2),
    ;

    private final int type;

    public static UgcCategoryType of(int type) {
        return Arrays.stream(UgcCategoryType.values())
            .filter(item -> item.getType() == type)
            .findFirst()
            .orElse(ARTICLE_CATEGORY);
    }

}
