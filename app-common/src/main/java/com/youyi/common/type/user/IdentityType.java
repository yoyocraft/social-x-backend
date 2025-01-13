package com.youyi.common.type.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@AllArgsConstructor
public enum IdentityType {

    /**
     * 邮箱验证码验证
     */
    EMAIL_CAPTCHA,

    /**
     * 邮箱密码验证
     */
    EMAIL_PASSWORD,

    ;

    public static IdentityType of(String type) {
        return EnumUtils.getEnum(IdentityType.class, type, EMAIL_CAPTCHA);
    }

}
