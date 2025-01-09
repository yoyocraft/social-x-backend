package com.youyi.common.constant;

import java.util.regex.Pattern;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class ValidateRegex {

    public static final String VALIDATE_EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    public static final Pattern VALIDATE_EMAIL_PATTERN = Pattern.compile(VALIDATE_EMAIL_REGEX);
}
