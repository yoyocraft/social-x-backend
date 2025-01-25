package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import java.time.LocalDateTime;
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

    @SerializedName("authorId")
    private String authorId;

    @SerializedName("authorName")
    private String authorName;

    @SerializedName("authorAvatar")
    private String authorAvatar;

    @SerializedName("viewCount")
    private Long viewCount;

    @SerializedName("likeCount")
    private Long likeCount;

    @SerializedName("commentCount")
    private Long commentCount;

    @SerializedName("status")
    private String status;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("gmtCreate")
    private LocalDateTime gmtCreate;

    @SerializedName("gmtModified")
    private LocalDateTime gmtModified;
}
