package com.youyi.domain.user.core;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.collect.Lists;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.HotAuthorCacheInfo;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.model.UserLoginStateInfo;
import com.youyi.domain.user.repository.UserRelationRepository;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.domain.user.repository.relation.SuggestedUserInfo;
import com.youyi.domain.user.repository.relation.UserNode;
import com.youyi.domain.user.repository.relation.UserRelationship;
import com.youyi.domain.user.util.RecommendUtil;
import com.youyi.infra.cache.CacheKey;
import com.youyi.infra.cache.CacheRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.domain.user.constant.UserConstant.USER_LOGIN_STATE;
import static com.youyi.infra.cache.key.UserCacheKeyRepo.ofHotAuthorListKey;
import static com.youyi.infra.cache.key.UserCacheKeyRepo.ofUserFollowIdsKey;
import static com.youyi.infra.conf.core.Conf.getBooleanConfig;
import static com.youyi.infra.conf.core.ConfigKey.QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    private final CacheRepository cacheRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDO getCurrentUserInfo() {
        boolean queryFromDB = getBooleanConfig(QUERY_LOGIN_USER_INFO_FROM_DB_AB_SWITCH);
        if (queryFromDB) {
            logger.debug("[UserHelper] load current user from db");
            return loadCurrentUserFromDB();
        }
        return loadCurrentUserFromSession();
    }

    public UserDO loadCurrentUserFromSession() {
        Object loginId = StpUtil.getLoginId();
        String loginUserStateInfoJson = (String) StpUtil.getSessionByLoginId(loginId).get(USER_LOGIN_STATE);
        UserLoginStateInfo loginStateInfo = GsonUtil.fromJson(loginUserStateInfoJson, UserLoginStateInfo.class);
        UserDO userDO = new UserDO();
        userDO.fillUserInfo(loginStateInfo);
        return userDO;
    }

    public UserDO loadCurrentUserFromDB() {
        Object loginId = StpUtil.getLoginId();
        String userId = (String) loginId;
        return queryByUserId(userId);
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

    public void followUser(UserDO subscriber, UserDO creator) {
        // 1. 如果需要，创建用户节点
        createUserIfNeed(subscriber);
        createUserIfNeed(creator);

        // 2. 插入关注关系
        userRelationRepository.addFollowingUserRelationship(subscriber.getUserId(), creator.getUserId());
    }

    public void unfollowUser(UserDO subscriber, UserDO creator) {
        // 0. 这里不需要创建用户节点，因为用户节点已经存在了
        // 1. 删除关注关系
        userRelationRepository.deleteFollowingUserRelationship(subscriber.getUserId(), creator.getUserId());
    }

    public void createUserIfNeed(UserDO userDO) {
        Optional<UserNode> userNodeOptional = Optional.ofNullable(userRelationRepository.findByUserId(userDO.getUserId()));
        if (userNodeOptional.isPresent()) {
            return;
        }
        userRelationRepository.save(userDO.getUserId(), userDO.getNickname());
    }

    public void fillRelationshipInfo(UserDO userDO) {
        int followingCount = userRelationRepository.getFollowingCount(userDO.getUserId());
        int followerCount = userRelationRepository.getFollowerCount(userDO.getUserId());

        userDO.setFollowingCount(followingCount);
        userDO.setFollowerCount(followerCount);
    }

    public void fillUserInteractInfo(UserDO userDO) {
        UserDO currentUser = getCurrentUserInfo();
        // 当前用户是否关注了该用户
        userDO.setHasFollowed(
            Optional
                .ofNullable(userRelationRepository.queryFollowingUserRelations(currentUser.getUserId(), userDO.getUserId()))
                .isPresent()
        );
    }

    public void fillUserInteractInfo(List<UserDO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        List<String> creatorIds = users.stream().map(UserDO::getUserId).toList();
        UserDO currentUser = getCurrentUserInfo();
        List<String> followerIds = userRelationRepository.queryFollowingUserRelationsBatch(currentUser.getUserId(), creatorIds);
        Set<String> followerIdSet = Set.copyOf(followerIds);
        users.forEach(follower -> follower.setHasFollowed(followerIdSet.contains(follower.getUserId())));
    }

    public List<UserDO> queryFollowingUsers(UserDO userDO) {
        List<UserRelationship> followingUserRelations = userRelationRepository.getFollowingUsers(
            userDO.getUserId(),
            userDO.getCursor(),
            userDO.getSize()
        );
        if (CollectionUtils.isEmpty(followingUserRelations)) {
            return Collections.emptyList();
        }

        final Long nextCursor = followingUserRelations.get(followingUserRelations.size() - 1).getSince();
        userDO.setCursor(nextCursor);
        List<String> followingUserIds = followingUserRelations.stream()
            .map(UserRelationship::getTarget)
            .map(UserNode::getUserId)
            .toList();
        Map<String, UserDO> id2UserDOMapping = queryBatchByUserId(followingUserIds);
        return Lists.newArrayList(id2UserDOMapping.values());
    }

    public List<UserDO> queryFollowers(UserDO userDO) {
        List<UserRelationship> followerRelations = userRelationRepository.getFollowers(
            userDO.getUserId(),
            userDO.getCursor(),
            userDO.getSize()
        );
        if (CollectionUtils.isEmpty(followerRelations)) {
            return Collections.emptyList();
        }
        final Long nextCursor = followerRelations.get(followerRelations.size() - 1).getSince();
        userDO.setCursor(nextCursor);
        List<String> followerUserIds = followerRelations.stream()
            .map(UserRelationship::getTarget)
            .map(UserNode::getUserId)
            .toList();
        Map<String, UserDO> id2UserDOMapping = queryBatchByUserId(followerUserIds);
        return Lists.newArrayList(id2UserDOMapping.values());
    }

    public void polishUserFollowCache(UserDO currentUser, UserDO followUserInfo, boolean follow) {
        String followCacheKey = ofUserFollowIdsKey(currentUser.getUserId());
        // 如果是关注，则添加到缓存中，否则从缓存中移除
        if (follow) {
            cacheRepository.addToSet(followCacheKey, followUserInfo.getUserId(), CacheKey.USER_FOLLOW_IDS.getTtl());
            return;
        }
        cacheRepository.removeFromSet(followCacheKey, followUserInfo.getUserId());
    }

    public Set<String> queryFollowingUserIdsFromCache(UserDO userDO) {
        String followCacheKey = ofUserFollowIdsKey(userDO.getUserId());
        Set<Object> cachedFollowIds = cacheRepository.getSetMembers(followCacheKey);

        if (CollectionUtils.isNotEmpty(cachedFollowIds)) {
            return cachedFollowIds.stream()
                .map(String.class::cast)
                .collect(Collectors.toUnmodifiableSet());
        }

        // 从 neo4j 获取
        List<UserRelationship> allFollowingUsers = userRelationRepository.getAllFollowingUsers(userDO.getUserId());
        if (CollectionUtils.isEmpty(allFollowingUsers)) {
            return Collections.emptySet();
        }
        Set<String> followUserIds = allFollowingUsers.stream()
            .map(UserRelationship::getTarget)
            .filter(Objects::nonNull)
            .map(UserNode::getUserId)
            .collect(Collectors.toSet());
        cacheRepository.addToSet(followCacheKey, CacheKey.USER_FOLLOW_IDS.getTtl(), followUserIds);
        return followUserIds;
    }

    public List<UserDO> querySuggestedUsersUsingGraph(UserDO userDO) {
        List<SuggestedUserInfo> suggestedUserInfoList = userRelationRepository.getSuggestedUsers(userDO.getUserId(), userDO.getSize());
        if (CollectionUtils.isEmpty(suggestedUserInfoList)) {
            return Collections.emptyList();
        }

        Set<String> suggestedUserIds = suggestedUserInfoList.stream().map(SuggestedUserInfo::getSuggestedUserId).collect(Collectors.toSet());
        Map<String, UserDO> userId2DOMap = queryBatchByUserId(suggestedUserIds);
        return new ArrayList<>(userId2DOMap.values());
    }

    public List<UserDO> querySuggestedUserFromDatabase(UserDO currentUser) {
        if (CollectionUtils.isEmpty(currentUser.getPersonalizedTags())) {
            return Collections.emptyList();
        }

        List<UserInfoPO> suggestedUserInfos = userRepository.querySuggestedUsers(currentUser.buildToQueryUserInfoPO());
        if (CollectionUtils.isEmpty(suggestedUserInfos)) {
            return Collections.emptyList();
        }

        List<UserDO> suggestedUsers = suggestedUserInfos.stream().map(po -> {
            UserDO userDO = new UserDO();
            userDO.fillUserInfo(po);
            return userDO;
        }).toList();

        return RecommendUtil.recommendUsers(currentUser, suggestedUsers);
    }

    public List<HotAuthorCacheInfo> listHotUserCache() {
        String cacheKey = ofHotAuthorListKey();
        String hotUserJson = cacheRepository.getString(cacheKey);
        return GsonUtil.fromJson(hotUserJson, List.class, HotAuthorCacheInfo.class);
    }
}
