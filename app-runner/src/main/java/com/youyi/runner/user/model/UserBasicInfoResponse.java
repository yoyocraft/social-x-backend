package com.youyi.runner.user.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Setter
public class UserBasicInfoResponse extends BaseResponse {

    @SerializedName("userId")
    private String userId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("bio")
    private String bio;

    @SerializedName("personalizedTags")
    private List<String> personalizedTags;

    @SerializedName("workStartTime")
    private String workStartTime;

    @SerializedName("workDirection")
    private Integer workDirection;

    @SerializedName("jobTitle")
    private String jobTitle;

    @SerializedName("company")
    private String company;

    @SerializedName("desensitizedMobile")
    private String desensitizedMobile;

    @SerializedName("desensitizedEmail")
    private String desensitizedEmail;

    @SerializedName("role")
    private String role;

    @SerializedName("joinTime")
    public Long joinTime;

    @SerializedName("followerCount")
    public Long followerCount;

    @SerializedName("followingCount")
    public Long followingCount;

    @SerializedName("hasFollowed")
    private Boolean hasFollowed;
}
