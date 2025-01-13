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

    @SerializedName("user_id")
    private String userId;

    @SerializedName("nick_name")
    private String nickName;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("bio")
    private String bio;

    @SerializedName("personalized_tags")
    private List<String> personalizedTags;

    @SerializedName("location")
    private String location;

    @SerializedName("desensitized_mobile")
    private String desensitizedMobile;

    @SerializedName("desensitized_email")
    private String desensitizedEmail;

    @SerializedName("gender")
    private String gender;

    @SerializedName("role")
    private String role;
}
