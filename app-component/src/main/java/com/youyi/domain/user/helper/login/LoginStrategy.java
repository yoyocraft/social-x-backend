package com.youyi.domain.user.helper.login;

import com.youyi.domain.user.model.UserDO;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public interface LoginStrategy {

    void login(UserDO userDO);
}
