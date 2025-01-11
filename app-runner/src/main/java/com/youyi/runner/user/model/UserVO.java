package com.youyi.runner.user.model;

import com.youyi.common.base.BaseVO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Getter
@Setter
public class UserVO extends BaseVO {

    private String userId;
    private String nickName;
    private String avatar;
    private String bio;
    private List<String> personalizedTags;
    private String location;
    private String desensitizedMobile;
    private String desensitizedEmail;
    private String gender;
    private String role;
}
