package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public class UgcResponse extends BaseResponse {

    @SerializedName("ugcId")
    private String ugcId;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("summary")
    private String summary;

    @SerializedName("cover")
    private String cover;

    @SerializedName("attachmentUrls")
    private List<String> attachmentUrls;

    @SerializedName("author")
    private UserBasicInfoResponse author;

    @SerializedName("viewCount")
    private Long viewCount;

    @SerializedName("likeCount")
    private Long likeCount;

    @SerializedName("collectCount")
    private Long collectCount;

    @SerializedName("commentaryCount")
    private Long commentaryCount;

    @SerializedName("status")
    private String status;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("hasSolved")
    private Boolean hasSolved;

    @SerializedName("gmtCreate")
    private Long gmtCreate;

    @SerializedName("gmtModified")
    private Long gmtModified;

    @SerializedName("liked")
    private Boolean liked;

    @SerializedName("collected")
    private Boolean collected;

    @SerializedName("auditRet")
    public String auditRet;
}
