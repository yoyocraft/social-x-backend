package com.youyi.domain.ugc.core;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.RepositoryConstant.INIT_QUERY_CURSOR;
import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcService {

    private final UgcRepository ugcRepository;

    public void publishUgc(UgcDO ugcDO) {
        if (ugcDO.isNew()) {
            // new ugc
            ugcDO.create();
            ugcRepository.saveUgc(ugcDO.buildToSaveUgcDocument());
            return;
        }

        // find by ugcIc
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkStatusValidationBeforeUpdate(ugcDocument);
        ugcDO.fillBeforeUpdateWhenPublish(ugcDocument);
        ugcRepository.updateUgc(ugcDO.buildToUpdateUgcDocumentWhenPublish());
    }

    public List<UgcDocument> querySelfUgcWithCursor(UgcDO ugcDO) {
        UserDO author = ugcDO.getAuthor();
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        LocalDateTime cursor = getTimeCursor(ugcDO);

        return ugcRepository.queryByKeywordAndStatusForSelfWithCursor(
            ugcDO.getKeyword(),
            ugcDO.getStatus().name(),
            author.getUserId(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryWithUgcIdCursor(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        LocalDateTime cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryMainPageInfoWithIdCursor(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatusType.PUBLISHED.name(),
            cursor,
            ugcDO.getSize()
        );
    }

    void checkStatusValidationBeforeUpdate(UgcDocument ugcDocument) {
        // 私密的稿件禁止修改
        if (UgcStatusType.PRIVATE.name().equals(ugcDocument.getStatus())) {
            throw AppBizException.of(OPERATION_DENIED, "私密稿件禁止修改！");
        }
    }

    LocalDateTime getTimeCursor(UgcDO ugcDO) {
        LocalDateTime cursor;
        if (INIT_QUERY_CURSOR.equals(ugcDO.getCursor())) {
            cursor = LocalDateTime.now();
        } else {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getCursor());
            checkNotNull(ugcDocument);
            cursor = ugcDocument.getGmtModified();
        }
        return cursor;
    }
}
