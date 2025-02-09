package com.youyi.common.type.task;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public enum TaskType {

    UNKNOWN,
    UGC_DELETE_EVENT,
    COMMENTARY_DELETE_EVENT,
    UGC_ADOPT_EVENT,
    ;

    public static TaskType of(String type) {
        return EnumUtils.getEnum(TaskType.class, type, UNKNOWN);
    }
}
