package com.youyi.common.type;

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

    EMAIL_CAPTCHA,
    EMAIL_PASSWORD,

    ;

    public static IdentityType of(String type) {
        return EnumUtils.getEnum(IdentityType.class, type, EMAIL_CAPTCHA);
    }

}
