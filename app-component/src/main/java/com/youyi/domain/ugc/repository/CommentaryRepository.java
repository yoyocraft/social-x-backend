package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.ugc.model.CommentaryExtraData;
import com.youyi.domain.ugc.repository.dao.CommentaryDAO;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Repository
@RequiredArgsConstructor
public class CommentaryRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentaryRepository.class);

    private final CommentaryDAO commentaryDAO;

    public void saveCommentary(CommentaryDocument commentaryDocument) {
        try {
            checkNotNull(commentaryDocument);
            commentaryDAO.save(commentaryDocument);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void saveAllCommentary(Collection<CommentaryDocument> commentaryDocuments) {
        try {
            checkState(CollectionUtils.isNotEmpty(commentaryDocuments));
            commentaryDAO.saveAll(commentaryDocuments);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public CommentaryDocument queryByCommentaryId(String commentaryId) {
        try {
            checkNotNull(commentaryId);
            return commentaryDAO.queryByCommentaryId(commentaryId);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<CommentaryDocument> queryRootCommentaryWithTimeCursor(String ugcId, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return commentaryDAO.queryRootCommentaryWithTimeCursor(ugcId, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<CommentaryDocument> queryByParentId(String parentId) {
        try {
            checkState(StringUtils.isNotBlank(parentId));
            return commentaryDAO.queryByParentId(parentId);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<CommentaryDocument> queryByParentId(Collection<String> parentId) {
        try {
            checkState(CollectionUtils.isNotEmpty(parentId));
            return commentaryDAO.queryByParentId(parentId);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<CommentaryDocument> queryWithTimeCursor(long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return commentaryDAO.queryWithTimeCursor(lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void deleteCommentary(String commentaryId) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            commentaryDAO.updateStatusByCommentaryId(commentaryId, CommentaryStatus.DELETED.name());
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void batchDeleteCommentary(List<String> commentaryIds) {
        try {
            checkState(CollectionUtils.isNotEmpty(commentaryIds));
            commentaryDAO.batchUpdateStatusByCommentaryId(commentaryIds, CommentaryStatus.DELETED.name());
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void incrLikeCount(String commentaryId, long incrLikeCount) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            commentaryDAO.updateCommentaryStatistics(commentaryId, incrLikeCount);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void updateCommentaryExtraData(String commentaryId, CommentaryExtraData extraData) {
        try {
            checkState(StringUtils.isNotBlank(commentaryId));
            commentaryDAO.updateCommentaryExtraData(commentaryId, extraData);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }
}
