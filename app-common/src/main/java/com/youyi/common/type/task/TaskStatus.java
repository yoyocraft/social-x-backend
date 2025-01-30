package com.youyi.common.type.task;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public enum TaskStatus {

    UNKNOWN,
    INIT,
    PROCESSING,
    SUCCESS,
    FAIL,
    ;

    public static TaskStatus of(String status) {
        return EnumUtils.getEnum(TaskStatus.class, status, UNKNOWN);
    }
}
