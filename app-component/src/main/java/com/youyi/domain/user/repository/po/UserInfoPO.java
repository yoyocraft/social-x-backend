package com.youyi.domain.user.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Getter
@Setter
public class UserInfoPO extends BasePO {

    private String userId;
    private String email;
    private String emailIv;
    private String phone;
    private String phoneIv;

    private String nickname;
    private String avatar;
    private String workStartTime;
    private Integer workDirection;
    private String bio;
    private String personalizedTags;
    private String jobTitle;
    private String company;

    private Integer status;
    private String role;
}
