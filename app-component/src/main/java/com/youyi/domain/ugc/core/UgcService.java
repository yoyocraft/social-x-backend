package com.youyi.domain.ugc.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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

    private final UgcTpeContainer ugcTpeContainer;
    private final UgcRepository ugcRepository;

    public void publishUgc(UgcDO ugcDO) {
        if (ugcDO.isNew()) {
            // new ugc
            ugcDO.create();
            ugcRepository.saveUgc(ugcDO.buildToSaveUgcDocument());
            return;
        }

        // find by ugcId
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkNotNull(ugcDocument);
        checkStatusValidationBeforeUpdate(ugcDocument);
        ugcDO.fillBeforeUpdateWhenPublish(ugcDocument);
        ugcRepository.updateUgc(ugcDO.buildToUpdateUgcDocumentWhenPublish());
    }

    public List<UgcDocument> querySelfUgcWithCursor(UgcDO ugcDO) {
        UserDO author = ugcDO.getAuthor();
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);

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
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryMainPageInfoWithIdCursor(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDO> fillAuthorAndCursorInfo(List<UgcDocument> ugcDocumentList, Map<String, UserDO> id2UserInfoMap) {
        // 下一次查询的 cursor
        final String nextCursor = Optional.ofNullable(
            CollectionUtils.isEmpty(ugcDocumentList)
                ? null
                : ugcDocumentList.get(ugcDocumentList.size() - 1).getUgcId()
        ).orElse(SymbolConstant.EMPTY);
        return ugcDocumentList.stream()
            .map(ugcDocument -> {
                UserDO author = id2UserInfoMap.get(ugcDocument.getAuthorId());
                UgcDO ugcDO = new UgcDO();
                ugcDO.fillWithUgcDocument(ugcDocument);
                ugcDO.setAuthor(author);
                ugcDO.setCursor(nextCursor);
                return ugcDO;
            }).toList();
    }

    public void asyncIncrUgcViewCount(UgcDO ugcDO, UserDO currentUser) {
        boolean canIncr = checkIncrCondition(ugcDO, currentUser);
        if (!canIncr) {
            return;
        }
        ugcTpeContainer.getUgcStatisticsExecutor().execute(() -> incrUgcViewCount(ugcDO));
    }

    private void incrUgcViewCount(UgcDO ugcDO) {
        // TODO youyi 2025/1/29 内存缓冲下，批量更新
        ugcRepository.incrViewCount(ugcDO.getUgcId(), 1);
    }

    private void checkStatusValidationBeforeUpdate(UgcDocument ugcDocument) {
        // 私密的稿件禁止修改
        if (UgcStatus.PRIVATE.name().equals(ugcDocument.getStatus())) {
            throw AppBizException.of(OPERATION_DENIED, "私密稿件禁止修改！");
        }
    }

    private long getTimeCursor(UgcDO ugcDO) {
        long cursor;
        if (INIT_QUERY_CURSOR.equals(ugcDO.getCursor())) {
            cursor = System.currentTimeMillis();
        } else {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getCursor());
            checkNotNull(ugcDocument);
            cursor = ugcDocument.getGmtModified();
        }
        return cursor;
    }

    private boolean checkIncrCondition(UgcDO ugcDO, UserDO currentUser) {
        // 仅公开稿件才可增加浏览量
        if (ugcDO.getStatus() != UgcStatus.PUBLISHED) {
            return false;
        }

        // 非作者查看可以增加浏览量
        return !ugcDO.getAuthor().getUserId().equals(currentUser.getUserId());
    }
}
