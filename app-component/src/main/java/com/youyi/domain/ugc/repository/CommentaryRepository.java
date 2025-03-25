package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.type.CommentaryStatus;
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

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Repository
@RequiredArgsConstructor
public class CommentaryRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentaryRepository.class);

    private final CommentaryDAO commentaryDAO;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MONGODB;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MONGODB_ERROR;
    }

    public void saveCommentary(CommentaryDocument commentaryDocument) {
        checkNotNull(commentaryDocument);
        executeWithExceptionHandling(() -> commentaryDAO.save(commentaryDocument));
    }

    public void saveAllCommentary(Collection<CommentaryDocument> commentaryDocuments) {
        checkState(CollectionUtils.isNotEmpty(commentaryDocuments));
        executeWithExceptionHandling(() -> commentaryDAO.saveAll(commentaryDocuments));
    }

    public CommentaryDocument queryByCommentaryId(String commentaryId) {
        checkNotNull(commentaryId);
        return executeWithExceptionHandling(() -> commentaryDAO.queryByCommentaryId(commentaryId));
    }

    public List<CommentaryDocument> queryRootCommentaryWithTimeCursor(String ugcId, long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> commentaryDAO.queryRootCommentaryWithTimeCursor(ugcId, lastCursor, size));
    }

    public List<CommentaryDocument> queryByParentId(String parentId) {
        checkState(StringUtils.isNotBlank(parentId));
        return executeWithExceptionHandling(() -> commentaryDAO.queryByParentId(parentId));
    }

    public List<CommentaryDocument> queryByParentId(Collection<String> parentId) {
        checkState(CollectionUtils.isNotEmpty(parentId));
        return executeWithExceptionHandling(() -> commentaryDAO.queryByParentId(parentId));
    }

    public List<CommentaryDocument> queryByCommentatorId(String commentatorId) {
        checkState(StringUtils.isNotBlank(commentatorId));
        return executeWithExceptionHandling(() -> commentaryDAO.queryByCommentatorId(commentatorId));
    }

    public List<CommentaryDocument> queryWithTimeCursor(long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> commentaryDAO.queryWithTimeCursor(lastCursor, size));
    }

    public void deleteCommentary(String commentaryId) {
        checkState(StringUtils.isNotBlank(commentaryId));
        executeWithExceptionHandling(() -> commentaryDAO.updateStatusByCommentaryId(commentaryId, CommentaryStatus.DELETED.name()));
    }

    public void batchDeleteCommentary(List<String> commentaryIds) {
        checkState(CollectionUtils.isNotEmpty(commentaryIds));
        executeWithExceptionHandling(() -> commentaryDAO.batchUpdateStatusByCommentaryId(commentaryIds, CommentaryStatus.DELETED.name()));
    }

    public void incrLikeCount(String commentaryId, long incrLikeCount) {
        checkState(StringUtils.isNotBlank(commentaryId));
        executeWithExceptionHandling(() -> commentaryDAO.updateCommentaryStatistics(commentaryId, incrLikeCount));
    }

    public void updateCommentaryExtraData(String commentaryId, CommentaryExtraData extraData) {
        checkState(StringUtils.isNotBlank(commentaryId));
        executeWithExceptionHandling(() -> commentaryDAO.updateCommentaryExtraData(commentaryId, extraData));
    }
}
