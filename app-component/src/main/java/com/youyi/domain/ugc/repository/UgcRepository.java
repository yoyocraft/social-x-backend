package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.dao.UgcDAO;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.type.UgcStatus;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.SymbolConstant.EMPTY;
import static java.util.Collections.emptyList;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Repository
@RequiredArgsConstructor
public class UgcRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcRepository.class);

    private final UgcDAO ugcDAO;

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

    public void saveUgc(UgcDocument ugcDocument) {
        checkNotNull(ugcDocument);
        executeWithExceptionHandling(() -> ugcDAO.save(ugcDocument));
    }

    public void saveAllUgc(Collection<UgcDocument> ugcDocuments) {
        checkState(CollectionUtils.isNotEmpty(ugcDocuments));
        executeWithExceptionHandling(() -> ugcDAO.saveAll(ugcDocuments));
    }

    public void deleteUgc(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        executeWithExceptionHandling(() -> ugcDAO.updateStatusByUgcId(ugcId, UgcStatus.DELETED.name()));
    }

    public List<UgcDocument> querySelfUgc(String ugcType, String ugcStatus, String authorId, long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, ugcType, EMPTY, ugcStatus, authorId, emptyList(), lastCursor, size));
    }

    public UgcDocument queryByUgcId(String ugcId) {
        checkState(StringUtils.isNotBlank(ugcId));
        return executeWithExceptionHandling(() -> ugcDAO.queryByUgcId(ugcId));
    }

    public List<UgcDocument> queryBatchByUgcId(List<String> ugcIds) {
        checkState(Objects.nonNull(ugcIds) && !ugcIds.isEmpty());
        return executeWithExceptionHandling(() -> ugcDAO.queryBatchByUgcId(ugcIds));
    }

    public List<UgcDocument> queryBatchByAuthorId(String authorId) {
        checkState(StringUtils.isNotBlank(authorId));
        return executeWithExceptionHandling(() -> ugcDAO.queryBatchByAuthorId(authorId));
    }

    public void updateUgc(UgcDocument ugcDocument) {
        checkNotNull(ugcDocument);
        executeWithExceptionHandling(() -> ugcDAO.updateByUgcId(ugcDocument));
    }

    public List<UgcDocument> queryByStatusWithTimeCursor(String ugcType, String ugcStatus, long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, ugcType, EMPTY, ugcStatus, emptyList(), emptyList(), lastCursor, size));
    }

    public List<UgcDocument> queryWithTimeCursor(String keyword, String categoryId, String ugcType, String ugcStatus, List<String> tags,
        long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(keyword, ugcType, categoryId, ugcStatus, emptyList(), tags, lastCursor, size));
    }

    public List<UgcDocument> queryByAuthorWithTimeCursor(String categoryId, String ugcType, String ugcStatus, String authorId,
        long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0 && StringUtils.isNotBlank(authorId));
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, authorId, emptyList(), lastCursor, size));
    }

    public List<UgcDocument> queryByAuthorWithTimeCursor(String categoryId, String ugcType, String ugcStatus, Collection<String> authorIds,
        long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, authorIds, emptyList(), lastCursor, size));
    }

    public List<UgcDocument> queryByTagWithTimeCursor(Collection<String> tags, String categoryId, String ugcStatus, long lastCursor, int size) {
        checkState(System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, EMPTY, categoryId, ugcStatus, emptyList(), tags, lastCursor, size));
    }

    public void incrUgcStatisticCount(String ugcId, long incrViewCount, long incrLikeCount, long incrCollectCount, long incrCommentaryCount) {
        checkState(StringUtils.isNotBlank(ugcId));
        executeWithExceptionHandling(() -> ugcDAO.updateUgcStatistics(ugcId, incrViewCount, incrLikeCount, incrCollectCount, incrCommentaryCount));
    }

    public List<UgcDocument> queryByExtraDataWithTimeCursor(String categoryId, String ugcType, String ugcStatus, Boolean hasSolved,
        long lastCursor, int size) {
        checkState(Objects.nonNull(hasSolved) && System.currentTimeMillis() >= lastCursor && size > 0);
        return executeWithExceptionHandling(() -> ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, EMPTY, emptyList(), hasSolved, lastCursor, size));
    }
}
