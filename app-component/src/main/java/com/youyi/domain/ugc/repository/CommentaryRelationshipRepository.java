package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.dao.CommentaryRelationshipDAO;
import com.youyi.domain.ugc.repository.relation.CommentaryNode;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
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
public class CommentaryRelationshipRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentaryRelationshipRepository.class);

    private final CommentaryRelationshipDAO commentaryRelationshipDAO;

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

    public CommentaryNode save(String commentaryId) {
        checkState(StringUtils.isNotBlank(commentaryId));
        return executeWithExceptionHandling(() -> commentaryRelationshipDAO.save(commentaryId));
    }

    public void addLikeRelationship(String commentaryId, String userId) {
        checkState(StringUtils.isNoneBlank(commentaryId, userId));
        executeWithExceptionHandling(() -> commentaryRelationshipDAO.addLikeRelationship(commentaryId, userId));
    }

    public void deleteLikeRelationship(String commentaryId, String userId) {
        checkState(StringUtils.isNoneBlank(commentaryId, userId));
        executeWithExceptionHandling(() -> commentaryRelationshipDAO.deleteLikeRelationship(commentaryId, userId));
    }

    public UgcInteractRelationship queryLikeRelationship(String commentaryId, String userId) {
        checkState(StringUtils.isNoneBlank(commentaryId, userId));
        return executeWithExceptionHandling(() -> commentaryRelationshipDAO.queryLikeRelationship(commentaryId, userId));
    }

    public List<String> queryLikeRelationships(List<String> commentaryIds, String userId) {
        checkState(StringUtils.isNotBlank(userId));
        if (commentaryIds == null || commentaryIds.isEmpty()) {
            return Collections.emptyList();
        }
        return executeWithExceptionHandling(() -> commentaryRelationshipDAO.queryLikeRelationships(commentaryIds, userId));
    }

    public CommentaryNode findByCommentaryId(String commentaryId) {
        checkState(StringUtils.isNotBlank(commentaryId));
        return executeWithExceptionHandling(() -> commentaryRelationshipDAO.findByCommentaryId(commentaryId));
    }

    public void deleteAllLikeRelationships(String commentaryId) {
        checkState(StringUtils.isNotBlank(commentaryId));
        executeWithExceptionHandling(() -> commentaryRelationshipDAO.deleteAllLikeRelationships(commentaryId));
    }

    public void deleteAllLikeRelationships(List<String> commentaryIds) {
        if (commentaryIds == null || commentaryIds.isEmpty()) {
            return;
        }
        executeWithExceptionHandling(() -> commentaryRelationshipDAO.deleteAllLikeRelationships(commentaryIds));
    }

    public void deleteCommentaryNode(List<String> commentaryIds) {
        if (commentaryIds == null || commentaryIds.isEmpty()) {
            return;
        }
        executeWithExceptionHandling(() -> commentaryRelationshipDAO.deleteCommentaryNode(commentaryIds));
    }
}
