package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
@Getter
@Setter
public class UgcSummaryGenerateRequest extends BaseRequest {

    @SerializedName("ugcId")
    private String ugcId;
}
