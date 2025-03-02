package com.youyi.common.type.ugc;

import com.youyi.common.constant.SymbolConstant;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * USER_GENERATED_CONTENT
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
public enum UgcType {

    /**
     * 所有
     */
    ALL(SymbolConstant.EMPTY),

    DRAFT("草稿"),

    /**
     * 文章
     */
    ARTICLE("文章"),

    /**
     * 动态帖子
     */
    POST("帖子"),

    /**
     * 问答
     */
    QUESTION("问答"),
    ;

    private final String desc;

    UgcType(String desc) {
        this.desc = desc;
    }

    public static UgcType of(String type) {
        return EnumUtils.getEnum(UgcType.class, type, ALL);
    }
}
