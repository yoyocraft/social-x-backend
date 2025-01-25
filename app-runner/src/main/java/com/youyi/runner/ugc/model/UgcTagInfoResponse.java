package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcTagInfoResponse extends BaseResponse {

    @SerializedName("tagId")
    private String tagId;

    @SerializedName("tagName")
    private String tagName;

    @SerializedName("priority")
    private Integer priority;

    @SerializedName("selected")
    private Boolean selected;
}
