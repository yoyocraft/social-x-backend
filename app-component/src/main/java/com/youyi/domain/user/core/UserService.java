package com.youyi.domain.user.core;

import cn.dev33.satoken.stp.StpUtil;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.domain.user.model.relation.UserNode;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.domain.user.repository.relation.UserRelationRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.common.type.conf.ConfigKey.QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH;
import static com.youyi.infra.conf.core.SystemConfigService.getBooleanConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserDO getCurrentUserInfo() {
        boolean queryFromDB = getBooleanConfig(QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH);
        if (queryFromDB) {
            LOGGER.info("[UserHelper] load current user from db");
            return loadCurrentUserFromDB();
        }
        return loadCurrentUserFromSession();
    }

    public UserDO loadCurrentUserFromSession() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        String loginUserStateInfoJson = (String) StpUtil.getSessionByLoginId(loginId).get(USER_LOGIN_STATE);
        UserLoginStateInfo loginStateInfo = GsonUtil.fromJson(loginUserStateInfoJson, UserLoginStateInfo.class);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(loginStateInfo);
        return userDO;
    }

    public UserDO loadCurrentUserFromDB() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        String userId = (String) loginId;
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(userId);
        checkNotNull(userInfoPO);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(userInfoPO);
        return userDO;
    }

    public UserDO queryByUserId(String userId) {
        UserInfoPO userInfoPO = userRepository.queryUserInfoByUserId(userId);
        checkNotNull(userInfoPO);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(userInfoPO);
        return userDO;
    }

    public Map<String, UserDO> queryBatchByUserId(Collection<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Map.of();
        }
        List<UserInfoPO> userInfoPOList = userRepository.queryUserInfoByUserIds(userIds);
        if (CollectionUtils.isEmpty(userInfoPOList)) {
            return Map.of();
        }

        return userInfoPOList.stream()
            .collect(
                Collectors.toMap(
                    UserInfoPO::getUserId,
                    userInfoPO -> {
                        UserDO userDO = new UserDO();
                        userDO.fillUserInfo(userInfoPO);
                        return userDO;
                    }
                )
            );
    }

    public void followUser(UserDO currentUser, UserDO followUser) {
        // 1. 如果需要，创建用户节点
        createUserIfNeed(currentUser);
        createUserIfNeed(followUser);

        // 2. 插入关注关系
        userRelationRepository.addFollowingUserRelationship(currentUser.getUserId(), followUser.getUserId());
    }

    public void unfollowUser(UserDO currentUser, UserDO unfollowUser) {
        // 0. 这里不需要创建用户节点，因为用户节点已经存在了
        // 1. 删除关注关系
        userRelationRepository.deleteFollowingUserRelationship(currentUser.getUserId(), unfollowUser.getUserId());
    }

    private void createUserIfNeed(UserDO userDO) {
        Optional<UserNode> userNodeOptional = Optional.ofNullable(userRelationRepository.findByUserId(userDO.getUserId()));
        if (userNodeOptional.isPresent()) {
            return;
        }
        userRelationRepository.save(userDO.getUserId(), userDO.getNickName());
    }
}
