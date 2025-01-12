package com.youyi.domain.notification.param;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseParam;
import com.youyi.common.type.BizType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Getter
@Setter
public class CaptchaNotifyParam extends BaseParam {

    /**
     * @see BizType
     */
    @SerializedName("biz_type")
    private String bizType;

    @SerializedName("email")
    private String email;
}
