package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.dao.UgcDAO;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcRepository.class);

    private final UgcDAO ugcDAO;

    public void saveUgc(UgcDocument ugcDocument) {
        try {
            checkNotNull(ugcDocument);
            ugcDAO.save(ugcDocument);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }

    public Page<UgcDocument> queryByKeywordAndStatusForSelf(String keyword, String ugcStatus, String authorId, int page, int size) {
        try {
            checkState(page >= 0 && size > 0);
            return ugcDAO.queryByKeywordAndStatusForSelf(keyword, ugcStatus, authorId, page, size);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MONGODB, InfraCode.MONGODB_ERROR, e);
            throw AppSystemException.of(InfraCode.MONGODB_ERROR, e);
        }
    }
}
