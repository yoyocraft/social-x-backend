package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.ugc.repository.dao.UgcDAO;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.util.LogUtil.infraLog;

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

    public void deleteUgc(String ugcId) {
        try {
            checkState(StringUtils.isNotBlank(ugcId));
            ugcDAO.updateStatusByUgcId(ugcId, UgcStatus.DELETED.name());
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByKeywordAndStatusForSelfWithCursor(String keyword, String ugcType, String ugcStatus, String authorId,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryByKeywordForSelfWithCursor(keyword, ugcType, ugcStatus, authorId, lastCursor, size);
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

    public List<UgcDocument> queryByUgcIds(List<String> ugcIds) {
        try {
            checkState(Objects.nonNull(ugcIds) && !ugcIds.isEmpty());
            return ugcDAO.queryByUgcIds(ugcIds);
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

    public List<UgcDocument> queryByStatusWithTimeCursor(String ugcStatus, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryByStatusWithTimeCursor(ugcStatus, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryWithTimeCursor(String categoryId, String type, String ugcStatus, List<String> tags, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryInfoWithIdCursor(categoryId, type, ugcStatus, Collections.emptyList(), tags, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryUserPageInfoWithTimeCursor(String categoryId, String type, String ugcStatus, String authorId,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0 && StringUtils.isNotBlank(authorId));
            return ugcDAO.queryInfoWithIdCursor(categoryId, type, ugcStatus, Collections.singletonList(authorId), Collections.emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryFollowPageInfoWithTimeCursor(String categoryId, String type, String ugcStatus, Collection<String> authorIds,
        long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryInfoWithIdCursor(categoryId, type, ugcStatus, authorIds, Collections.emptyList(), lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public List<UgcDocument> queryByTagWithTimeCursor(Collection<String> tags, long lastCursor, int size) {
        try {
            checkState(System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryByTagWithTimeCursor(tags, lastCursor, size);
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

    public List<UgcDocument> queryInfoWithIdCursorAndExtraData(String categoryId, String type, String ugcStatus,
        Boolean hasSolved, long lastCursor, int size) {
        try {
            checkState(Objects.nonNull(hasSolved) && System.currentTimeMillis() >= lastCursor && size > 0);
            return ugcDAO.queryInfoWithIdCursorAndExtraData(categoryId, type, ugcStatus, hasSolved, lastCursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }
}
