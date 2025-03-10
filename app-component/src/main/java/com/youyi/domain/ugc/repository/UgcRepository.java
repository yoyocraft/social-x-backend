package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.ugc.repository.dao.UgcDAO;
import com.youyi.domain.ugc.repository.document.UgcDocument;
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
import static com.youyi.common.util.LogUtil.infraLog;
import static java.util.Collections.emptyList;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Repository
@RequiredArgsConstructor
public class UgcRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcRepository.class);

    private final UgcDAO ugcDAO;

    public void saveUgc(UgcDocument ugcDocument) {
        try {
            checkNotNull(ugcDocument);
            ugcDAO.save(ugcDocument);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void saveAllUgc(Collection<UgcDocument> ugcDocuments) {
        try {
            checkState(CollectionUtils.isNotEmpty(ugcDocuments));
            ugcDAO.saveAll(ugcDocuments);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void deleteUgc(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcDAO.updateStatusByUgcId(ugcId, UgcStatus.DELETED.name());
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> querySelfUgc(String ugcType, String ugcStatus, String authorId, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(EMPTY, ugcType, EMPTY, ugcStatus, authorId, emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public UgcDocument queryByUgcId(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            return ugcDAO.queryByUgcId(ugcId);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryBatchByUgcId(List<String> ugcIds) {
        try {
            checkState(Objects.nonNull(ugcIds) && !ugcIds.isEmpty());
            return ugcDAO.queryBatchByUgcId(ugcIds);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void updateUgc(UgcDocument ugcDocument) {
        try {
            checkNotNull(ugcDocument);
            ugcDAO.updateByUgcId(ugcDocument);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void updateUgcStatus(String ugcId, String ugcStatus) {
        try {
            checkState(StringUtils.isNotBlank(ugcId) && StringUtils.isNotBlank(ugcStatus));
            ugcDAO.updateStatusByUgcId(ugcId, ugcStatus);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByStatusWithTimeCursor(String ugcType, String ugcStatus, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(EMPTY, ugcType, EMPTY, ugcStatus, emptyList(), emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryWithTimeCursor(String keyword, String categoryId, String ugcType, String ugcStatus, List<String> tags,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(keyword, ugcType, categoryId, ugcStatus, emptyList(), tags, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByAuthorWithTimeCursor(String categoryId, String ugcType, String ugcStatus, String authorId,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0 && StringUtils.isNotBlank(authorId));
            return ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, authorId, emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByAuthorWithTimeCursor(String categoryId, String ugcType, String ugcStatus, Collection<String> authorIds,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, authorIds, emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByTagWithTimeCursor(Collection<String> tags, String ugcStatus, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(EMPTY, EMPTY, EMPTY, ugcStatus, emptyList(), tags, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public void incrUgcStatisticCount(String ugcId, long incrViewCount, long incrLikeCount, long incrCollectCount, long incrCommentaryCount) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcDAO.updateUgcStatistics(ugcId, incrViewCount, incrLikeCount, incrCollectCount, incrCommentaryCount);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByExtraDataWithTimeCursor(String categoryId, String ugcType, String ugcStatus, Boolean hasSolved,
        long lastCursor, int size) {
        try {
            checkState(Objects.nonNull(hasSolved) && System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryWithTimeCursor(EMPTY, ugcType, categoryId, ugcStatus, EMPTY, emptyList(), hasSolved, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }
}
