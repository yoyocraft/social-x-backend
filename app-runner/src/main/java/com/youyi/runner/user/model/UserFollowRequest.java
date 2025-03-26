package com.youyi.runner.user.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Getter
@Setter
public class UserFollowRequest extends BaseRequest {

    @SerializedName("followUserId")
    private String followUserId;

    /**
     * true => 关注
     * false => 取消关注
     */
    @SerializedName("follow")
    private Boolean follow;
}
