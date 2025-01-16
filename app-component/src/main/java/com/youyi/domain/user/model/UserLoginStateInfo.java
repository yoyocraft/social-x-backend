package com.youyi.domain.user.model;

import com.youyi.common.type.user.UserRoleType;
import com.youyi.common.type.user.UserStatusType;
import com.youyi.common.type.user.WorkDirectionType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 个人信息主页登录展示信息，登录态信息
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Builder
public class UserLoginStateInfo {

    private String userId;
    private String nickName;
    private String desensitizedMobile;
    private String desensitizedEmail;
    private String avatar;
    private String bio;
    private List<String> personalizedTags;
    private String workStartTime;
    private WorkDirectionType workDirection;
    private String jobTitle;
    private String company;

    private UserRoleType role;
    private UserStatusType status;

}
