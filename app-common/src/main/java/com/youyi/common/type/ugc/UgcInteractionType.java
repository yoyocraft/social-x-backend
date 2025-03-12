package com.youyi.common.type.ugc;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public enum UgcInteractionType {
    UNKNOWN,

    LIKE,

    COLLECT,

    /**
     * 采纳
     */
    ADOPT,

    /**
     * 精选
     */
    FEATURED,
    ;

    public static UgcInteractionType of(String interactionType) {
        return EnumUtils.getEnum(UgcInteractionType.class, interactionType, UNKNOWN);
    }
}
