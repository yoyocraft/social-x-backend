package com.youyi.domain.user.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.helper.login.LoginStrategy;
import com.youyi.domain.user.helper.login.LoginStrategyFactory;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.domain.user.model.relation.UserRelationship;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.domain.user.repository.relation.UserRelationRepository;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.ReturnCode.NOT_LOGIN;
import static com.youyi.common.type.ReturnCode.PERMISSION_DENIED;
import static com.youyi.common.util.IdSeqUtil.genUserVerifyCaptchaToken;
import static com.youyi.infra.cache.repo.NotificationCacheRepo.ofEmailCaptchaKey;
import static com.youyi.infra.cache.repo.UserCacheRepo.USER_VERIFY_TOKEN_TTL;
import static com.youyi.infra.cache.repo.UserCacheRepo.ofUserVerifyTokenKey;

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
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    public void login(UserDO userDO) {
        LoginStrategy loginStrategy = loginStrategyFactory.getLoginStrategy(userDO.getIdentityType());
        loginStrategy.login(userDO);
    }

    public UserDO getCurrentUser() {
        checkLogin();
        return userService.getCurrentUserInfo();
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
        loadCurrentUserInfoForUpdate(userDO);
        // 2. 校验Token
        checkToken(userDO);
        // 3. 加密
        encryptPwd(userDO);
        // 4. 保存
        savePwd(userDO);
    }

    public void editUserInfo(UserDO userDO) {
        // 1. 获取当前用户信息
        UserDO currentUser = userService.getCurrentUserInfo();
        // 2. 校验
        checkCanEdit(userDO, currentUser);
        // 3. 更新
        doEditUserInfo(userDO);
        // 4. 更新登录态信息
        updateCurrentUserInfo();
    }

    public void followUser(UserDO userDO) {
        // 1. 获取当前用户信息
        UserDO currentUser = userService.getCurrentUserInfo();
        // 2. 校验
        checkFollowUser(userDO, currentUser);
        // 3. 关注 or 取消关注
        if (Boolean.TRUE.equals(userDO.getFollowFlag())) {
            doFollowUser(currentUser, userDO);
            return;
        }
        doUnfollowUser(currentUser, userDO);
    }

    private void doFollowUser(UserDO currentUser, UserDO userDO) {
        // 3. 幂等校验
        Optional<UserRelationship> hasFollowOptional = Optional.ofNullable(userRelationRepository.queryFollowingUserRelations(currentUser.getUserId(), userDO.getFollowingUserId()));
        if (hasFollowOptional.isPresent()) {
            return;
        }
        // 4. 获取关注用户信息
        UserDO followUserInfo = userService.queryByUserId(userDO.getFollowingUserId());
        // 5. 关注用户
        userService.followUser(currentUser, followUserInfo);
    }

    private void doUnfollowUser(UserDO currentUser, UserDO userDO) {
        // 3. 幂等校验
        Optional<UserRelationship> hasFollowOptional = Optional.ofNullable(userRelationRepository.queryFollowingUserRelations(currentUser.getUserId(), userDO.getFollowingUserId()));
        if (hasFollowOptional.isEmpty()) {
            return;
        }
        // 4. 获取取关用户信息
        UserDO unfollowUserInfo = userService.queryByUserId(userDO.getFollowingUserId());
        userService.unfollowUser(currentUser, unfollowUserInfo);
    }

    private void checkLogin() {
        boolean login = StpUtil.isLogin();
        if (!login) {
            throw AppBizException.of(NOT_LOGIN);
        }
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (Objects.isNull(loginId)) {
            throw AppBizException.of(NOT_LOGIN);
        }
    }

    private void checkCaptcha(UserDO userDO) {
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

    private void genTokenAndCleanCaptcha(UserDO userDO) {
        String verifyToken = genUserVerifyCaptchaToken();
        userDO.setVerifyCaptchaToken(verifyToken);
        String verifyTokenCacheKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getBizType());
        cacheManager.set(verifyTokenCacheKey, verifyToken, USER_VERIFY_TOKEN_TTL);
        cacheManager.delete(ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getBizType()));
    }

    private void checkToken(UserDO userDO) {
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

    private void encryptPwd(UserDO userDO) {
        userDO.initSalt();
        userDO.encryptPwd();
    }

    private void savePwd(UserDO userDO) {
        userDO.preSetPwd();
        UserAuthPO userAuthPO = userDO.buildToSaveUserAuthPO();
        // insert or update
        userRepository.insertOrUpdateUserAuth(userAuthPO);
    }

    private void checkCanEdit(UserDO editUserDO, UserDO currentUserDO) {
        if (currentUserDO.isAdmin()) {
            return;
        }

        if (currentUserDO.getUserId().equals(editUserDO.getUserId())) {
            return;
        }

        throw AppBizException.of(PERMISSION_DENIED, "无权限修改");
    }

    private void doEditUserInfo(UserDO userDO) {
        UserInfoPO userInfoPO = userDO.buildToUpdateUserInfoPO();
        userRepository.editUserInfo(userInfoPO);
    }

    private void updateCurrentUserInfo() {
        UserDO userDO = userService.loadCurrentUserFromDB();
        UserLoginStateInfo loginStateInfo = userDO.buildLoginStateInfo();
        StpUtil.login(userDO.getUserId());
        StpUtil.getSession().set(USER_LOGIN_STATE, GsonUtil.toJson(loginStateInfo));
    }

    private void loadCurrentUserInfoForUpdate(UserDO userDO) {
        UserDO currentUser = userService.loadCurrentUserFromSession();
        checkNotNull(currentUser);
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(currentUser.getUserId());
        userDO.fillUserInfo(userInfoPO);
    }

    private void checkFollowUser(UserDO userDO, UserDO currentUser) {
        if (currentUser.getUserId().equals(userDO.getUserId())) {
            throw AppBizException.of(PERMISSION_DENIED, "不可以关注自己哦～");
        }
    }
}
