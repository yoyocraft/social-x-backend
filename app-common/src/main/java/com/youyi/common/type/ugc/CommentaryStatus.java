package com.youyi.common.type.ugc;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
public enum CommentaryStatus {

    /**
     * 所有，for query
     */
    ALL,
    /**
     * 正常
     */
    NORMAL,
    /**
     * 敏感
     */
    SENSITIVE,
    /**
     * 已删除
     */
    DELETED,
    ;

    public static CommentaryStatus of(String status) {
        return EnumUtils.getEnum(CommentaryStatus.class, status, ALL);
    }
}
