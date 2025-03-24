package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
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
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Repository
@RequiredArgsConstructor
public class CommentaryRelationshipRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentaryRelationshipRepository.class);

    private final CommentaryRelationshipDAO commentaryRelationshipDAO;

    public CommentaryNode save(String commentaryId) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            return commentaryRelationshipDAO.save(commentaryId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void addLikeRelationship(String commentaryId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(commentaryId, userId));
            commentaryRelationshipDAO.addLikeRelationship(commentaryId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteLikeRelationship(String commentaryId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(commentaryId, userId));
            commentaryRelationshipDAO.deleteLikeRelationship(commentaryId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public UgcInteractRelationship queryLikeRelationship(String commentaryId, String userId) {
        try {
            checkState(StringUtils.isNoneBlank(commentaryId, userId));
            return commentaryRelationshipDAO.queryLikeRelationship(commentaryId, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public List<String> queryLikeRelationships(List<String> commentaryIds, String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            if (commentaryIds == null || commentaryIds.isEmpty()) {
                return Collections.emptyList();
            }
            return commentaryRelationshipDAO.queryLikeRelationships(commentaryIds, userId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public CommentaryNode findByCommentaryId(String commentaryId) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            return commentaryRelationshipDAO.findByCommentaryId(commentaryId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteAllLikeRelationships(String commentaryId) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            commentaryRelationshipDAO.deleteAllLikeRelationships(commentaryId);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteAllLikeRelationships(List<String> commentaryIds) {
        try {
            if (commentaryIds == null || commentaryIds.isEmpty()) {
                return;
            }
            commentaryRelationshipDAO.deleteAllLikeRelationships(commentaryIds);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }

    public void deleteCommentaryNode(List<String> commentaryIds) {
        try {
            if (commentaryIds == null || commentaryIds.isEmpty()) {
                return;
            }
            commentaryRelationshipDAO.deleteCommentaryNode(commentaryIds);
        } catch (Exception e) {
            infraLog(logger, InfraType.NEO4J, InfraCode.NEO4J_ERROR, e);
            throw AppSystemException.of(InfraCode.NEO4J_ERROR, e);
        }
    }
}
