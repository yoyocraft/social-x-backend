package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.dao.UgcRelationshipDAO;
import com.youyi.domain.ugc.repository.relation.UgcInteractInfo;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
import com.youyi.domain.ugc.repository.relation.UgcNode;
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
 * @date 2025/01/29
 */
@Repository
@RequiredArgsConstructor
public class UgcRelationshipRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcRelationshipRepository.class);

    private final UgcRelationshipDAO ugcRelationshipDAO;

    public UgcNode save(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            return ugcRelationshipDAO.save(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void addLikeRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            ugcRelationshipDAO.addLikeRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteLikeRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            ugcRelationshipDAO.deleteLikeRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void addCollectRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            ugcRelationshipDAO.addCollectRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteCollectRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            ugcRelationshipDAO.deleteCollectRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public UgcInteractRelationship queryLikeRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            return ugcRelationshipDAO.queryLikeRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryLikeRelationships(List<String> ugcIds, String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            if (ugcIds == null || ugcIds.isEmpty()) {
                return Collections.emptyList();
            }
            return ugcRelationshipDAO.queryLikeRelationships(ugcIds, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public UgcInteractRelationship queryCollectRelationship(String ugcId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(ugcId, userId));
            return ugcRelationshipDAO.queryCollectRelationship(ugcId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryCollectRelationships(List<String> ugcIds, String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            if (ugcIds == null || ugcIds.isEmpty()) {
                return Collections.emptyList();
            }
            return ugcRelationshipDAO.queryCollectRelationships(ugcIds, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<UgcInteractInfo> queryCollectedUgcIdsWithCursor(String userId, Long cursor, int limit) {
        try {
            checkState(StringUtils.isNotBlank(userId) && limit > 0);
            return ugcRelationshipDAO.queryCollectedUgcIdsWithCursor(userId, cursor, limit);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryAllCollectedUgcIds(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return ugcRelationshipDAO.queryAllCollectedUgcIds(userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public UgcNode findByUgcId(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            return ugcRelationshipDAO.findByUgcId(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteAllLikeRelationships(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcRelationshipDAO.deleteAllLikeRelationships(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteAllCollectRelationships(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcRelationshipDAO.deleteAllCollectRelationships(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteUgcNode(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcRelationshipDAO.deleteUgcNode(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }
}
