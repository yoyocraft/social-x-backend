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

    /**
     * 密码正则, 密码必须包含数字、小写字母、大写字母、特殊字符且长度在8-20位之间
     */
    public static final String VALIDATE_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
    public static final Pattern VALIDATE_PASSWORD_PATTERN = Pattern.compile(VALIDATE_PASSWORD_REGEX);

}
