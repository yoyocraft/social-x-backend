package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcTagQueryRequest extends BasePageRequest {

    /**
     * 首次查询时，传入 '0'
     */
    @SerializedName("cursor")
    private String cursor;
}
