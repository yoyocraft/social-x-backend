package com.youyi.runner.user.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import com.youyi.domain.user.type.IdentityType;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Setter
public class UserAuthenticateRequest extends BaseRequest {

    /**
     * @see IdentityType
     */
    @SerializedName("identityType")
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
