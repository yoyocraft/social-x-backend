package com.youyi.domain.user.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.helper.login.LoginStrategy;
import com.youyi.domain.user.helper.login.LoginStrategyFactory;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.youyi.common.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.ConfigKey.QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH;
import static com.youyi.common.type.ReturnCode.NOT_LOGIN;
import static com.youyi.infra.config.core.SystemConfigService.getBooleanConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Service
@RequiredArgsConstructor
public class UserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    private final LoginStrategyFactory loginStrategyFactory;

    public void login(UserDO userDO) {
        LoginStrategy loginStrategy = loginStrategyFactory.getLoginStrategy(userDO.getIdentityType());
        loginStrategy.login(userDO);
    }

    public UserDO getCurrentUser() {
        boolean login = StpUtil.isLogin();
        if (!login) {
            throw AppBizException.of(NOT_LOGIN);
        }
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (Objects.isNull(loginId)) {
            throw AppBizException.of(NOT_LOGIN);
        }

        boolean queryFromDB = getBooleanConfig(QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH);
        if (queryFromDB) {
            // TODO youyi 2025/1/11 从数据库查询用户信息
            String userId = (String) loginId;
            LOGGER.info("query login user info from db, userId:{}", userId);
            return new UserDO();
        }
        // 从 session 中获取
        String loginUserStateInfoJson = (String) StpUtil.getSessionByLoginId(loginId).get(USER_LOGIN_STATE);
        UserLoginStateInfo loginStateInfo = GsonUtil.fromJson(loginUserStateInfoJson, UserLoginStateInfo.class);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(loginStateInfo);
        return userDO;
    }

    public void logout() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        StpUtil.logout();
        StpUtil.getSessionByLoginId(loginId).delete(USER_LOGIN_STATE);
    }
}
