package com.youyi.common.type.ugc;

import org.apache.commons.lang3.EnumUtils;

/**
 * USER_GENERATED_CONTENT
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public enum UgcType {

    /**
     * 文章
     */
    ARTICLE,

    /**
     * 动态帖子
     */
    POST,

    /**
     * 问答
     */
    QUESTION,
    ;

    public static UgcType of(String type) {
        return EnumUtils.getEnum(UgcType.class, type, ARTICLE);
    }
}
