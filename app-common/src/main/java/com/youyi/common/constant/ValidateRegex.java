package com.youyi.common.constant;

import java.util.regex.Pattern;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class ValidateRegex {

    /**
     * 邮箱正则
     */
    public static final String VALIDATE_EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    public static final Pattern VALIDATE_EMAIL_PATTERN = Pattern.compile(VALIDATE_EMAIL_REGEX);

    /**
     * 六位数字正则，用于验证码的验证
     */
    public static final String VALIDATE_SIX_DIGITS_REGEX = "^\\d{6}$";
    public static final Pattern VALIDATE_SIX_DIGITS_PATTERN = Pattern.compile(VALIDATE_SIX_DIGITS_REGEX);
}
