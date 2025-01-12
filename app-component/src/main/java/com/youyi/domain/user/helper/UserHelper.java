package com.youyi.domain.user.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.helper.login.LoginStrategy;
import com.youyi.domain.user.helper.login.LoginStrategyFactory;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.youyi.common.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.ConfigKey.QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH;
import static com.youyi.common.type.ReturnCode.NOT_LOGIN;
import static com.youyi.common.util.RandomGenUtil.genUserVerifyCaptchaToken;
import static com.youyi.infra.cache.repo.NotificationCacheRepo.ofEmailCaptchaKey;
import static com.youyi.infra.cache.repo.UserCacheRepo.USER_VERIFY_TOKEN_TTL;
import static com.youyi.infra.cache.repo.UserCacheRepo.ofUserVerifyTokenKey;
import static com.youyi.infra.conf.core.SystemConfigService.getBooleanConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Service
@RequiredArgsConstructor
public class UserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    private final LoginStrategyFactory loginStrategyFactory;
    private final CacheManager cacheManager;

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

    public void verifyCaptcha(UserDO userDO) {
        checkCaptcha(userDO);
        genTokenAndCleanCaptcha(userDO);
    }

    void checkCaptcha(UserDO userDO) {
        String cacheCaptchaKey = ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getNotificationType());
        String systemCaptcha = cacheManager.getString(cacheCaptchaKey);
        if (StringUtils.isBlank(systemCaptcha)) {
            // 验证码过期
            throw AppBizException.of(ReturnCode.CAPTCHA_EXPIRED);
        }
        String userInputCaptcha = userDO.getToVerifiedCaptcha();

        if (!systemCaptcha.equals(userInputCaptcha)) {
            throw AppBizException.of(ReturnCode.CAPTCHA_ERROR);
        }
    }

    void genTokenAndCleanCaptcha(UserDO userDO) {
        String verifyToken = genUserVerifyCaptchaToken();
        userDO.setVerifyCaptchaToken(verifyToken);
        String verifyTokenCacheKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getNotificationType());
        cacheManager.set(verifyTokenCacheKey, verifyToken, USER_VERIFY_TOKEN_TTL);
        cacheManager.delete(ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getNotificationType()));
    }
}
