package com.youyi.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@AllArgsConstructor
public enum ReturnCode implements ErrorCode{

    SUCCESS("SUCCESS", "Success"),
    SYSTEM_ERROR("SYSTEM_ERROR", "System error"),
    INVALID_PARAM("INVALID_PARAM", "Invalid param"),
    ;

    private final String code;

    private final String message;
}
