package com.youyi.runner.media.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
@Setter
public class ImageUploadResponse extends BaseResponse {

    @SerializedName("accessUrl")
    private String accessUrl;
}
