package com.youyi.domain.user.param;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseParam;
import com.youyi.common.type.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Getter
@Setter
public class UserVerifyCaptchaParam extends BaseParam {

    @SerializedName("email")
    private String email;

    @SerializedName("captcha")
    private String captcha;

    /**
     * @see NotificationType
     */
    @SerializedName("biz_type")
    private String bizType;
}
