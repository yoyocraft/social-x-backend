package com.youyi.core.user.param;

import com.youyi.common.base.BaseParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
@Getter
@Setter
public class UserAddParam extends BaseParam {

    private String username;

    private Integer age;
}
