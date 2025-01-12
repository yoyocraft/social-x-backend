package com.youyi.domain.user.param;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseParam;
import com.youyi.common.type.user.IdentityType;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Setter
public class UserAuthenticateParam extends BaseParam {

    /**
     * @see IdentityType
     */
    @SerializedName("identity_type")
    private String identityType;

    /**
     * e.g. EMAIL, PHONE
     */
    @SerializedName("identifier")
    private String identifier;

    /**
     * e.g. captcha, password
     */
    @SerializedName("credential")
    private String credential;

    /**
     * 额外参数：图形验证码等
     */
    @SerializedName("extra")
    private Map<String, String> extra;
}
