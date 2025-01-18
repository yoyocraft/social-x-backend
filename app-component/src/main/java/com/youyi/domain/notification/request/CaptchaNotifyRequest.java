package com.youyi.domain.notification.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import com.youyi.common.type.BizType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Getter
@Setter
public class CaptchaNotifyRequest extends BaseRequest {

    /**
     * @see BizType
     */
    @SerializedName("bizType")
    private String bizType;

    @SerializedName("email")
    private String email;
}
