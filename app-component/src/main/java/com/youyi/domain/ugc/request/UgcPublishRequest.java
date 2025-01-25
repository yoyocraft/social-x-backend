package com.youyi.domain.ugc.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
@Setter
public class UgcPublishRequest extends BaseRequest {

    @SerializedName("ugcId")
    private String ugcId;

    @SerializedName("ugcType")
    private String ugcType;

    /**
     * 在发布动态的时候，不需要传入这个字段
     */
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("summary")
    private String summary;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("cover")
    private String cover;

    @SerializedName("attachmentUrls")
    private List<String> attachmentUrls;

    /**
     * 是否为草稿
     */
    @SerializedName("drafting")
    private Boolean drafting;

}
