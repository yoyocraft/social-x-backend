package com.youyi.domain.user.helper.login;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;

import static com.youyi.domain.user.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public interface LoginStrategy {

    void login(UserDO userDO);

    default void doLogin(UserDO userDO) {
        StpUtil.login(userDO.getUserId());
    }

    default void saveUserLoginState(UserDO userDO) {
        // 生成用户登录态信息
        UserLoginStateInfo loginStateInfo = userDO.buildLoginStateInfo();
        // 存储用户登录态
        StpUtil.getSession().set(USER_LOGIN_STATE, GsonUtil.toJson(loginStateInfo));
    }
}
