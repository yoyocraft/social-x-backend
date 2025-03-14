package com.youyi.domain.user.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.notification.core.NotificationManager;
import com.youyi.domain.ugc.model.HotAuthorCacheInfo;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.helper.login.LoginStrategy;
import com.youyi.domain.user.helper.login.LoginStrategyFactory;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.domain.user.repository.UserRelationRepository;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.domain.user.repository.relation.UserRelationship;
import com.youyi.infra.cache.CacheKey;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.domain.user.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.ReturnCode.PERMISSION_DENIED;
import static com.youyi.common.util.seq.IdSeqUtil.genUserVerifyCaptchaToken;
import static com.youyi.infra.cache.repo.UserCacheRepo.ofUserVerifyTokenKey;
import static com.youyi.infra.cache.repo.VerificationCacheRepo.ofEmailCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Service
@RequiredArgsConstructor
public class UserHelper {

    private final LoginStrategyFactory loginStrategyFactory;
    private final CacheManager cacheManager;
    private final NotificationManager notificationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    public void login(UserDO userDO) {
        LoginStrategy loginStrategy = loginStrategyFactory.getLoginStrategy(userDO.getIdentityType());
        loginStrategy.login(userDO);
    }

    public UserDO getCurrentUser() {
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        // 填充关注、粉丝信息
        userService.fillRelationshipInfo(currentUserInfo);
        return currentUserInfo;
    }

    public UserDO getUserInfo(UserDO userDO) {
        UserDO userInfo = userService.queryByUserId(userDO.getUserId());
        userService.fillRelationshipInfo(userInfo);
        return userInfo;
    }

    public void logout() {
        logoutAndClearUserLoginState();
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
        // 5. 清理验证码
        clearVerifyToken(userDO);
        // 6. 清理当前用户信息
        logoutAndClearUserLoginState();
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
        checkCanFollowUser(userDO, currentUser);
        // 3. 关注 or 取消关注
        if (Boolean.TRUE.equals(userDO.getFollowFlag())) {
            doFollowUser(currentUser, userDO);
            return;
        }
        doUnfollowUser(currentUser, userDO);
    }

    public List<UserDO> querySelfFollowingUsers(UserDO userDO) {
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        userDO.setUserId(currentUserInfo.getUserId());
        List<UserDO> followingUsers = userService.queryFollowingUsers(userDO);
        followingUsers.forEach(followingUser -> {
            followingUser.setHasFollowed(Boolean.TRUE);
            // 设置下一次查询的 cursor
            followingUser.setCursor(userDO.getCursor());
        });
        return followingUsers;
    }

    public List<UserDO> queryFollowers(UserDO userDO) {
        List<UserDO> followers = userService.queryFollowers(userDO);
        if (CollectionUtils.isEmpty(followers)) {
            return Collections.emptyList();
        }
        final Long nextCursor = userDO.getCursor();
        userService.fillUserInteractInfo(followers);
        followers.forEach(follower -> {
            follower.setCursor(nextCursor);
        });
        return followers;
    }

    public List<UserDO> querySuggestedUsers(UserDO userDO) {
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        userDO.setUserId(currentUserInfo.getUserId());
        List<UserDO> userDOList = userService.querySuggestedUsers(userDO);
        userService.fillUserInteractInfo(userDOList);
        return userDOList;
    }

    public List<UserDO> listHotUser() {
        List<HotAuthorCacheInfo> cacheInfos = userService.listHotUserCache();
        List<String> hotUserIds = cacheInfos.stream().map(HotAuthorCacheInfo::getAuthorId).toList();
        Map<String, UserDO> hotUserMap = userService.queryBatchByUserId(hotUserIds);
        List<UserDO> users = cacheInfos.stream().map(
            cacheInfo -> {
                UserDO user = hotUserMap.get(cacheInfo.getAuthorId());
                user.setHotScore(cacheInfo.getHotScore());
                return user;
            }
        ).toList();
        userService.fillUserInteractInfo(users);
        return users;
    }

    private void logoutAndClearUserLoginState() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        StpUtil.logout();
        StpUtil.getSessionByLoginId(loginId).delete(USER_LOGIN_STATE);
    }

    private void doFollowUser(UserDO currentUser, UserDO userDO) {
        // 1. 幂等校验
        Optional<UserRelationship> hasFollowOptional = Optional.ofNullable(
            userRelationRepository.queryFollowingUserRelations(
                currentUser.getUserId(),
                userDO.getFollowUserId()
            )
        );
        if (hasFollowOptional.isPresent()) {
            return;
        }
        // 2. 获取关注用户信息
        UserDO toFollowUserInfo = userService.queryByUserId(userDO.getFollowUserId());
        // 3. 关注用户
        userService.followUser(currentUser, toFollowUserInfo);
        // 4. 更新缓存信息
        userService.polishUserFollowCache(currentUser, toFollowUserInfo, true);
        // 5. 发送通知给用户
        notificationManager.sendUserFollowNotification(currentUser, toFollowUserInfo);
    }

    private void doUnfollowUser(UserDO currentUser, UserDO userDO) {
        // 3. 幂等校验
        Optional<UserRelationship> hasFollowOptional = Optional.ofNullable(
            userRelationRepository.queryFollowingUserRelations(
                currentUser.getUserId(),
                userDO.getFollowUserId()
            )
        );
        if (hasFollowOptional.isEmpty()) {
            return;
        }
        // 4. 获取取关用户信息
        UserDO unfollowUserInfo = userService.queryByUserId(userDO.getFollowUserId());
        userService.unfollowUser(currentUser, unfollowUserInfo);
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
        // 缓存 token
        cacheManager.set(verifyTokenCacheKey, verifyToken, CacheKey.USER_VERIFY_TOKEN.getTtl());
        // 清理验证码
        cacheManager.delete(ofEmailCaptchaKey(userDO.getOriginalEmail(), userDO.getBizType()));
    }

    private void checkToken(UserDO userDO) {
        String cacheVerifyTokenKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getBizType());
        String systemVerifyToken = (String) cacheManager.get(cacheVerifyTokenKey);

        if (StringUtils.isBlank(systemVerifyToken)) {
            throw AppBizException.of(ReturnCode.VERIFY_TOKEN_EXPIRED);
        }

        if (!systemVerifyToken.equals(userDO.getVerifyCaptchaToken())) {
            throw AppBizException.of(ReturnCode.ILLEGAL_OPERATION);
        }
    }

    private void clearVerifyToken(UserDO userDO) {
        String cacheVerifyTokenKey = ofUserVerifyTokenKey(userDO.getOriginalEmail(), userDO.getBizType());
        cacheManager.delete(cacheVerifyTokenKey);
    }

    private void encryptPwd(UserDO userDO) {
        userDO.initSalt();
        userDO.encryptPwd();
    }

    private void savePwd(UserDO userDO) {
        userDO.preSetPwd();
        UserAuthPO userAuthPO = userDO.buildToSaveUserAuthPO();
        userRepository.insertOrUpdateUserAuth(userAuthPO);
    }

    private void checkCanEdit(UserDO editUserDO, UserDO currentUserDO) {
        if (currentUserDO.isAdmin()) {
            return;
        }

        if (currentUserDO.getUserId().equals(editUserDO.getUserId())) {
            return;
        }

        throw AppBizException.of(PERMISSION_DENIED, "您没有权限修改该用户的信息～");
    }

    private void doEditUserInfo(UserDO userDO) {
        UserInfoPO userInfoPO = userDO.buildToUpdateUserInfoPO();
        userRepository.editUserInfo(userInfoPO);
    }

    private void updateCurrentUserInfo() {
        // 获取数据库最新的用户信息
        UserDO userDO = userService.loadCurrentUserFromDB();
        UserLoginStateInfo loginStateInfo = userDO.buildLoginStateInfo();
        StpUtil.getSession().set(USER_LOGIN_STATE, GsonUtil.toJson(loginStateInfo));
    }

    private void loadCurrentUserInfoForUpdate(UserDO userDO) {
        UserDO currentUser = userService.getCurrentUserInfo();
        checkNotNull(currentUser);
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(currentUser.getUserId());
        userDO.fillUserInfo(userInfoPO);
    }

    private void checkCanFollowUser(UserDO userDO, UserDO currentUser) {
        if (currentUser.getUserId().equals(userDO.getUserId())) {
            throw AppBizException.of(PERMISSION_DENIED, "不可以关注自己哦～");
        }
    }

}
