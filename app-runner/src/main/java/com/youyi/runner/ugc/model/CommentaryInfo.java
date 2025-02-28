package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryInfo {

    @SerializedName("commentaryId")
    private String commentaryId;

    @SerializedName("parentId")
    private String parentId;

    @SerializedName("ugcId")
    private String ugcId;

    @SerializedName("commentatorId")
    private String commentatorId;

    @SerializedName("commentatorNickname")
    private String commentatorNickname;

    @SerializedName("commentatorAvatar")
    private String commentatorAvatar;

    @SerializedName("commentary")
    private String commentary;

    @SerializedName("likeCount")
    private Long likeCount;

    @SerializedName("gmtCreate")
    private Long gmtCreate;

    @SerializedName("gmtModified")
    private Long gmtModified;

    @SerializedName("status")
    private String status;

    @SerializedName("attachmentUrls")
    private List<String> attachmentUrls;

    @SerializedName("adopted")
    private Boolean adopted;

    @SerializedName("liked")
    private Boolean liked;
}
