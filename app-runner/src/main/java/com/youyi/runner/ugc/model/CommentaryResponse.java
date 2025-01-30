package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryResponse extends BaseResponse {

    @SerializedName("topCommentary")
    private CommentaryInfo topCommentary;

    @SerializedName("replyList")
    private List<CommentaryInfo> replyList;
}
