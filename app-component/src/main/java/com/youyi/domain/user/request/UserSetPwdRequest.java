package com.youyi.domain.user.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Getter
@Setter
public class UserSetPwdRequest extends BaseRequest {

    @SerializedName("new_password")
    private String newPassword;

    @SerializedName("confirm_password")
    private String confirmPassword;
}
