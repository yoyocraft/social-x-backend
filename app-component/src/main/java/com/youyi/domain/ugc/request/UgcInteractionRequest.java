package com.youyi.domain.ugc.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
public class UgcInteractionRequest extends BaseRequest {

    @SerializedName("interactionType")
    String interactionType;

    @SerializedName("targetId")
    String targetId;

    /**
     * 点赞 / 取消点赞
     * 收藏 / 取消收藏
     */
    @SerializedName("interact")
    Boolean interact;

}
