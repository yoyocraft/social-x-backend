package com.youyi.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@AllArgsConstructor
public enum ReturnCode implements ErrorCode {

    SUCCESS("SUCCESS", ""),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统繁忙，请稍后再试"),
    INVALID_PARAM("INVALID_PARAM", "参数非法"),
    CAPTCHA_EXPIRED("CAPTCHA_EXPIRED", "验证码过期，请重新获取"),
    CAPTCHA_ERROR("CAPTCHA_ERROR", "验证码错误"),

    NOT_LOGIN("NOT_LOGIN", "未登录"),
    ;

    private final String code;

    private final String message;
}
