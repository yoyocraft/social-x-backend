package com.youyi.domain.user.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import com.youyi.common.type.BizType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Getter
@Setter
public class UserVerifyCaptchaRequest extends BaseRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("captcha")
    private String captcha;

    /**
     * @see BizType
     */
    @SerializedName("biz_type")
    private String bizType;
}
