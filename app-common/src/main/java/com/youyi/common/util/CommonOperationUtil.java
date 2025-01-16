package com.youyi.common.util;

import com.youyi.common.exception.AppBizException;

import static com.youyi.common.type.ReturnCode.TOO_MANY_REQUEST;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
public class CommonOperationUtil {

    public static void tooManyRequestError() {
        throw AppBizException.of(TOO_MANY_REQUEST);
    }
}
