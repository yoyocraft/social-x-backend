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
public class UgcCategoryInfoResponse extends BaseResponse {

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("priority")
    private Integer priority;

    @SerializedName("icon")
    private String icon;

    @SerializedName("qaTemplate")
    private String qaTemplate;

    @SerializedName("qaSuggestion")
    private String qaSuggestion;
}
