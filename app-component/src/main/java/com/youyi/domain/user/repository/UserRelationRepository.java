package com.youyi.domain.user.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.user.repository.dao.UserRelationDAO;
import com.youyi.domain.user.repository.relation.SuggestedUserInfo;
import com.youyi.domain.user.repository.relation.UserNode;
import com.youyi.domain.user.repository.relation.UserRelationship;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Repository
@RequiredArgsConstructor
public class UserRelationRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationRepository.class);

    private final UserRelationDAO userRelationDAO;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.NEO4J;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.NEO4J_ERROR;
    }

    public UserNode save(String userId, String nickname) {
        checkState(StringUtils.isNoneBlank(userId, nickname));
        return executeWithExceptionHandling(() -> userRelationDAO.save(userId, nickname));
    }

    public UserNode findByUserId(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userRelationDAO.findByUserId(userId));
    }

    public List<UserRelationship> queryFollowingUserRelations(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userRelationDAO.queryFollowingUserRelations(userId));
    }

    public UserRelationship queryFollowingUserRelations(String subscriberId, String creatorId) {
        checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
        return executeWithExceptionHandling(() -> userRelationDAO.queryFollowingUserRelations(subscriberId, creatorId));
    }

    public List<String> queryFollowingUserRelationsBatch(String subscriberId, List<String> creatorIds) {
        checkState(StringUtils.isNotBlank(subscriberId));
        if (creatorIds == null || creatorIds.isEmpty()) {
            return Collections.emptyList();
        }
        return executeWithExceptionHandling(() -> userRelationDAO.queryFollowingUserRelationsBatch(subscriberId, creatorIds));
    }

    public List<String> queryFollowingUserRelations(String subscriberId, List<String> creatorIds) {
        checkState(StringUtils.isNotBlank(subscriberId));
        if (creatorIds == null || creatorIds.isEmpty()) {
            return Collections.emptyList();
        }
        return executeWithExceptionHandling(() -> userRelationDAO.queryFollowingUserRelations(subscriberId, creatorIds));
    }

    public void addFollowingUserRelationship(String subscriberId, String creatorId) {
        checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
        executeWithExceptionHandling(() -> userRelationDAO.addFollowingUserRelationship(subscriberId, creatorId));
    }

    public void deleteFollowingUserRelationship(String subscriberId, String creatorId) {
        checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
        executeWithExceptionHandling(() -> userRelationDAO.deleteFollowingUserRelationship(subscriberId, creatorId));
    }

    public int getFollowingCount(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userRelationDAO.getFollowingCount(userId));
    }

    public int getFollowerCount(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userRelationDAO.getFollowerCount(userId));
    }

    public List<UserRelationship> getFollowingUsers(String userId, Long cursor, int limit) {
        checkState(StringUtils.isNotBlank(userId) && limit > 0);
        return executeWithExceptionHandling(() -> userRelationDAO.getFollowingUsers(userId, cursor, limit));
    }

    public List<UserRelationship> getFollowers(String userId, Long cursor, int limit) {
        checkState(StringUtils.isNotBlank(userId) && limit > 0);
        return executeWithExceptionHandling(() -> userRelationDAO.getFollowers(userId, cursor, limit));
    }

    public List<UserRelationship> getAllFollowingUsers(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userRelationDAO.getAllFollowingUsers(userId));
    }

    public List<SuggestedUserInfo> getSuggestedUsers(String userId, int limit) {
        checkState(StringUtils.isNotBlank(userId) && limit > 0);
        return executeWithExceptionHandling(() -> userRelationDAO.getSuggestedUsers(userId, limit));
    }
}
