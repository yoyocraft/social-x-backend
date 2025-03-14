package com.youyi.runner.verification.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/14
 */
@Getter
@Setter
public class ImageCaptchaResponse extends BaseResponse {

    @SerializedName("captchaId")
    private String captchaId;

    @SerializedName("image")
    private String image;
}
