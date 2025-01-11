package com.youyi.domain.user.param;

import com.youyi.common.type.IdentityType;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Setter
public class UserAuthenticateParam {

    /**
     * @see IdentityType
     */
    private String identityType;

    /**
     * e.g. EMAIL, PHONE
     */
    private String identifier;

    /**
     * e.g. captcha, password
     */
    private String credential;

    /**
     * 额外参数：图形验证码等
     */
    private Map<String, String> extra;
}
