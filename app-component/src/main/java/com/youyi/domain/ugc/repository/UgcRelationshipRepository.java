package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
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

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Repository
@RequiredArgsConstructor
public class UgcRelationshipRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcRelationshipRepository.class);

    private final UgcRelationshipDAO ugcRelationshipDAO;

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

    public UgcNode save(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.save(ugcId));
    }

    public void addLikeRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.addLikeRelationship(ugcId, userId));
    }

    public void deleteLikeRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.deleteLikeRelationship(ugcId, userId));
    }

    public void addCollectRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.addCollectRelationship(ugcId, userId));
    }

    public void deleteCollectRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.deleteCollectRelationship(ugcId, userId));
    }

    public UgcInteractRelationship queryLikeRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryLikeRelationship(ugcId, userId));
    }

    public List<String> queryLikeRelationships(List<String> ugcIds, String userId) {
        checkState(StringUtils.isNotBlank(userId));
        if (ugcIds == null || ugcIds.isEmpty()) {
            return Collections.emptyList();
        }
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryLikeRelationships(ugcIds, userId));
    }

    public UgcInteractRelationship queryCollectRelationship(String ugcId, String userId) {
        checkState(StringUtils.isNoneBlank(ugcId, userId));
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryCollectRelationship(ugcId, userId));
    }

    public List<String> queryCollectRelationships(List<String> ugcIds, String userId) {
        checkState(StringUtils.isNotBlank(userId));
        if (ugcIds == null || ugcIds.isEmpty()) {
            return Collections.emptyList();
        }
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryCollectRelationships(ugcIds, userId));
    }

    public List<UgcInteractInfo> queryCollectedUgcIdsWithCursor(String userId, Long cursor, int limit) {
        checkState(StringUtils.isNotBlank(userId) && limit > 0);
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryCollectedUgcIdsWithCursor(userId, cursor, limit));
    }

    public List<String> queryAllCollectedUgcIds(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.queryAllCollectedUgcIds(userId));
    }

    public UgcNode findByUgcId(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        return executeWithExceptionHandling(() -> ugcRelationshipDAO.findByUgcId(ugcId));
    }

    public void deleteAllLikeRelationships(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.deleteAllLikeRelationships(ugcId));
    }

    public void deleteAllCollectRelationships(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.deleteAllCollectRelationships(ugcId));
    }

    public void deleteUgcNode(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        executeWithExceptionHandling(() -> ugcRelationshipDAO.deleteUgcNode(ugcId));
    }
}
