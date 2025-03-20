package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/20
 */
@Getter
@Setter
public class UgcStatisticResponse extends BaseResponse {

    @SerializedName("articleCount")
    private Long articleCount;

    @SerializedName("postCount")
    private Long postCount;

    @SerializedName("questionCount")
    private Long questionCount;

    @SerializedName("collectCount")
    private Long collectCount;

    /**
     * 获赞数
     */
    @SerializedName("likeCount")
    private Long likeCount;

    /**
     * 发表评论数
     */
    @SerializedName("commentaryCount")
    private Long commentaryCount;
}
