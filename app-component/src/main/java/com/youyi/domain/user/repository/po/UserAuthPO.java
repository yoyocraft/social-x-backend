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
public class UserAuthPO extends BasePO {

    private String userId;
    private String identityType;
    private String identifier;
    private String credential;
    private String salt;

}
