package com.youyi.core.user.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
@Getter
@Setter
public class UserPO extends BasePO {

    private String name;
    private Integer age;
}
