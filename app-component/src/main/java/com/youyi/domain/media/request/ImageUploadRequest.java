package com.youyi.domain.media.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
@Setter
public class ImageUploadRequest extends BaseRequest {

    @SerializedName("source")
    private String source;
}
