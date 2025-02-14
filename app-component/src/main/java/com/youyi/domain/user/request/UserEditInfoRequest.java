package com.youyi.domain.user.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/13
 */
@Getter
@Setter
public class UserEditInfoRequest extends BaseRequest {

    @SerializedName("userId")
    private String userId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("workStartTime")
    private String workStartTime;

    @SerializedName("workDirection")
    private Integer workDirection;

    @SerializedName("bio")
    private String bio;

    @SerializedName("personalizedTags")
    private List<String> personalizedTags;

    @SerializedName("jobTitle")
    private String jobTitle;

    @SerializedName("company")
    private String company;

    @SerializedName("avatar")
    private String avatar;
}
