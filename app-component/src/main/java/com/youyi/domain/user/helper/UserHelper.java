package com.youyi.domain.user.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.helper.login.LoginStrategy;
import com.youyi.domain.user.helper.login.LoginStrategyFactory;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.ReturnCode.NOT_LOGIN;
import static com.youyi.common.type.ReturnCode.PERMISSION_DENIED;
import static com.youyi.common.type.conf.ConfigKey.QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH;
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
    private final UserRepository userRepository;

    public void login(UserDO userDO) {
        LoginStrategy loginStrategy = loginStrategyFactory.getLoginStrategy(userDO.getIdentityType());
        loginStrategy.login(userDO);
    }

    public UserDO getCurrentUser() {
        checkLogin();
        return doGetCurrentUser();
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

    public void setPwd(UserDO userDO) {
        // 1. 获取当前用户信息
        loadCurrentUserInfo(userDO);
        // 2. 校验Token
        checkToken(userDO);
        // 3. 加密
        encryptPwd(userDO);
        // 4. 保存
        savePwd(userDO);
    }

    public void editUserInfo(UserDO userDO) {
        // 1. 获取当前用户信息
        UserDO currentUser = doGetCurrentUser();
        // 2. 校验
        checkCanEdit(userDO, currentUser);
        // 3. 更新
        doEditUserInfo(userDO);
        // 4. 更新登录态信息
        updateCurrentUserInfo();
    }

    UserDO doGetCurrentUser() {
        boolean queryFromDB = getBooleanConfig(QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH);
        if (queryFromDB) {
            LOGGER.info("[UserHelper] load current user from db");
            return loadCurrentUserFromDB();
        }
        return loadCurrentUserFromSession();
    }

    UserDO loadCurrentUserFromSession() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        String loginUserStateInfoJson = (String) StpUtil.getSessionByLoginId(loginId).get(USER_LOGIN_STATE);
        UserLoginStateInfo loginStateInfo = GsonUtil.fromJson(loginUserStateInfoJson, UserLoginStateInfo.class);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(loginStateInfo);
        return userDO;
    }

    UserDO loadCurrentUserFromDB() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        String userId = (String) loginId;
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(userId);
        checkNotNull(userInfoPO);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(userInfoPO);
        return userDO;
    }

    void checkLogin() {
        boolean login = StpUtil.isLogin();
        if (!login) {
            throw AppBizException.of(NOT_LOGIN);
        }
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (Objects.isNull(loginId)) {
            throw AppBizException.of(NOT_LOGIN);
        }
    }

    void checkCaptcha(UserDO userDO) {
        String cacheCaptchaKey = ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getBizType());
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
        String verifyTokenCacheKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getBizType());
        cacheManager.set(verifyTokenCacheKey, verifyToken, USER_VERIFY_TOKEN_TTL);
        cacheManager.delete(ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getBizType()));
    }

    void loadCurrentUserInfo(UserDO userDO) {
        UserDO currentUser = loadCurrentUserFromSession();
        checkNotNull(currentUser);
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(currentUser.getUserId());
        userDO.fillUserInfo(userInfoPO);
    }

    void checkToken(UserDO userDO) {
        LOGGER.debug("user:{} check verify token", userDO.getUserId());
        String cacheVerifyTokenKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getBizType());
        String systemVerifyToken = (String) cacheManager.get(cacheVerifyTokenKey);

        if (StringUtils.isBlank(systemVerifyToken)) {
            throw AppBizException.of(ReturnCode.VERIFY_TOKEN_EXPIRED);
        }

        if (!systemVerifyToken.equals(userDO.getVerifyCaptchaToken())) {
            throw AppBizException.of(ReturnCode.ILLEGAL_OPERATION);
        }
    }

    void encryptPwd(UserDO userDO) {
        userDO.initSalt();
        userDO.encryptPwd();
    }

    void savePwd(UserDO userDO) {
        userDO.preSetPwd();
        UserAuthPO userAuthPO = userDO.buildToSaveUserAuthPO();
        // insert or update
        userRepository.insertOrUpdateUserAuth(userAuthPO);
    }

    void checkCanEdit(UserDO editUserDO, UserDO currentUserDO) {
        if (currentUserDO.isAdmin()) {
            return;
        }

        if (currentUserDO.getUserId().equals(editUserDO.getUserId())) {
            return;
        }

        throw AppBizException.of(PERMISSION_DENIED, "无权限修改");
    }

    void doEditUserInfo(UserDO userDO) {
        UserInfoPO userInfoPO = userDO.buildToUpdateUserInfoPO();
        userRepository.editUserInfo(userInfoPO);
    }

    void updateCurrentUserInfo() {
        UserDO userDO = loadCurrentUserFromDB();
        UserLoginStateInfo loginStateInfo = userDO.buildLoginStateInfo();
        StpUtil.login(userDO.getUserId());
        StpUtil.getSession().set(USER_LOGIN_STATE, GsonUtil.toJson(loginStateInfo));
    }
}
