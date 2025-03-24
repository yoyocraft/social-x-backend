package com.youyi.domain.user.repository;

import com.youyi.common.exception.AppSystemException;
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
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Repository
@RequiredArgsConstructor
public class UserRelationRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationRepository.class);

    private final UserRelationDAO userRelationDAO;

    public UserNode save(String userId, String nickname) {
        try {
            checkState(StringUtils.isNoneBlank(userId, nickname));
            return userRelationDAO.save(userId, nickname);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public UserNode findByUserId(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userRelationDAO.findByUserId(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<UserRelationship> queryFollowingUserRelations(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userRelationDAO.queryFollowingUserRelations(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public UserRelationship queryFollowingUserRelations(String subscriberId, String creatorId) {
        try {
            checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
            return userRelationDAO.queryFollowingUserRelations(subscriberId, creatorId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryFollowingUserRelationsBatch(String subscriberId, List<String> creatorIds) {
        try {
            checkState(StringUtils.isNotBlank(subscriberId));
            if (creatorIds == null || creatorIds.isEmpty()) {
                return Collections.emptyList();
            }
            return userRelationDAO.queryFollowingUserRelationsBatch(subscriberId, creatorIds);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryFollowingUserRelations(String subscriberId, List<String> creatorIds) {
        try {
            checkState(StringUtils.isNotBlank(subscriberId));
            if (creatorIds == null || creatorIds.isEmpty()) {
                return Collections.emptyList();
            }
            return userRelationDAO.queryFollowingUserRelations(subscriberId, creatorIds);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void addFollowingUserRelationship(String subscriberId, String creatorId) {
        try {
            checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
            userRelationDAO.addFollowingUserRelationship(subscriberId, creatorId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteFollowingUserRelationship(String subscriberId, String creatorId) {
        try {
            checkState(StringUtils.isNoneBlank(subscriberId, creatorId));
            userRelationDAO.deleteFollowingUserRelationship(subscriberId, creatorId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public int getFollowingCount(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userRelationDAO.getFollowingCount(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public int getFollowerCount(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userRelationDAO.getFollowerCount(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<UserRelationship> getFollowingUsers(String userId, Long cursor, int limit) {
        try {
            checkState(StringUtils.isNotBlank(userId) && limit > 0);
            return userRelationDAO.getFollowingUsers(userId, cursor, limit);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<UserRelationship> getFollowers(String userId, Long cursor, int limit) {
        try {
            checkState(StringUtils.isNotBlank(userId) && limit > 0);
            return userRelationDAO.getFollowers(userId, cursor, limit);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<UserRelationship> getAllFollowingUsers(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userRelationDAO.getAllFollowingUsers(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<SuggestedUserInfo> getSuggestedUsers(String userId, int limit) {
        try {
            checkState(StringUtils.isNotBlank(userId) && limit > 0);
            return userRelationDAO.getSuggestedUsers(userId, limit);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }
}
