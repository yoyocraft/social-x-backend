package com.youyi.common.type.ugc;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public enum UgcStatusType {

    /**
     * 草稿
     */
    DRAFT,
    /**
     * 审核中
     */
    AUDITING,
    /**
     * 审核不通过
     */
    REJECTED,
    /**
     * 已发布
     */
    PUBLISHED,
    /**
     * 仅我可见
     */
    PRIVATE,
    /**
     * 已删除，数据库字段
     */
    DELETED,
    ;

    public static UgcStatusType of(String status) {
        return EnumUtils.getEnum(UgcStatusType.class, status, DRAFT);
    }
}
