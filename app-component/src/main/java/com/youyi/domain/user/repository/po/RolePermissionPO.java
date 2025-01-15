package com.youyi.domain.user.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Getter
@Setter
public class RolePermissionPO extends BasePO {

    private String role;
    private String permissions;
}
