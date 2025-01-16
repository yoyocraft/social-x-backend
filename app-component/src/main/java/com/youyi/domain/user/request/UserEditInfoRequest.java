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

    @SerializedName("user_id")
    private String userId;

    @SerializedName("nick_name")
    private String nickName;

    /**
     * yyyy-MM
     */
    @SerializedName("work_start_time")
    private String workStartTime;

    @SerializedName("work_direction")
    private Integer workDirection;

    @SerializedName("bio")
    private String bio;

    @SerializedName("personalized_tags")
    private List<String> personalizedTags;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("company")
    private String company;

    @SerializedName("avatar")
    private String avatar;
}
