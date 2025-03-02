package com.youyi.domain.ugc.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BasePageRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public class UgcQueryRequest extends BasePageRequest {

    @SerializedName("ugcStatus")
    private String ugcStatus;

    @SerializedName("ugcType")
    private String ugcType;

    @SerializedName("keyword")
    private String keyword;

    @SerializedName("ugcId")
    private String ugcId;

    /**
     * 首次查询时，传入 '0'
     */
    @SerializedName("cursor")
    private String cursor;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("authorId")
    private String authorId;

    @SerializedName("qaStatus")
    private Boolean qaStatus;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("timeCursor")
    private Long timeCursor;
}
