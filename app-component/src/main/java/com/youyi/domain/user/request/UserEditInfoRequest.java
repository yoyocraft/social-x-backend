package com.youyi.domain.user.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import java.time.LocalDate;
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

    @SerializedName("nick_name")
    private String nickName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("date_of_birth")
    private LocalDate dateOfBirth;

    @SerializedName("bio")
    private String bio;

    @SerializedName("personalized_tags")
    private List<String> personalizedTags;

    @SerializedName("location")
    private String location;

    @SerializedName("avatar")
    private String avatar;
}
