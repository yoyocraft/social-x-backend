package com.youyi.common.util.param;

import lombok.Getter;

import static com.youyi.common.constant.ErrorCodeConstant.SUCCESS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
@Getter
public class CheckResult {

    private static final CheckResult SUCCESS_RESULT = new CheckResult(SUCCESS, SUCCESS);

    private final String code;
    private final String message;

    private CheckResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CheckResult success() {
        return SUCCESS_RESULT;
    }

    public static CheckResult of(String code, String message) {
        return new CheckResult(code, message);
    }

    public static boolean isSuccess(CheckResult checkResult) {
        return SUCCESS_RESULT.equals(checkResult);
    }
}
