package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Getter
@Setter
public class CommentaryPublishRequest extends BaseRequest {

    @SerializedName("ugcId")
    private String ugcId;

    @SerializedName("parentId")
    private String parentId;

    @SerializedName("commentary")
    private String commentary;

    @SerializedName("attachmentUrls")
    private List<String> attachmentUrls;
}
