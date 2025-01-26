package com.youyi.domain.ugc.helper;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.core.UgcService;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;
import static com.youyi.domain.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Service
@RequiredArgsConstructor
public class UgcHelper {

    private final UserService userService;
    private final UgcService ugcService;

    private final UgcRepository ugcRepository;

    public void publishUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        ugcService.publishUgc(ugcDO);
    }

    public void deleteUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkSelfAuthor(ugcDO, ugcDocument);
        ugcRepository.deleteUgc(ugcDO.getUgcId());
    }

    public List<UgcDO> querySelfUgc(UgcDO ugcDO) {
        // 1. 查询作者信息
        fillCurrUserAsAuthorInfo(ugcDO);
        // 2. 查询
        List<UgcDocument> ugcDocuments = ugcService.querySelfUgcWithCursor(ugcDO);
        // 3. 封装作者信息和游标信息
        UserDO author = ugcDO.getAuthor();
        List<UgcDO> ugcInfoList = userService.fillAuthorAndCursorInfo(ugcDocuments, ImmutableMap.of(author.getUserId(), author));
        // 4. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcInfoList);
        return ugcInfoList;
    }

    public UgcDO queryByUgcId(UgcDO ugcDO) {
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        return UGC_ASSEMBLER.toDO(ugcDocument);
    }

    public void updateUgcStatus(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkSelfAuthor(ugcDO, ugcDocument);
        checkStatusValidation(ugcDO, ugcDocument);
        ugcRepository.updateUgcStatus(ugcDO.getUgcId(), ugcDO.getStatus().name());
    }

    public List<UgcDO> queryByCursorForMainPage(UgcDO ugcDO) {
        // 1. 游标查询
        List<UgcDocument> ugcDocumentList = ugcService.queryWithUgcIdCursor(ugcDO);
        // 2. 批量查询作者信息
        Set<String> authorIds = ugcDocumentList.stream().map(UgcDocument::getAuthorId).collect(Collectors.toSet());
        Map<String, UserDO> id2UserInfoMap = userService.queryBatchByUserId(authorIds);
        // 3. 封装信息
        List<UgcDO> ugcDOList = userService.fillAuthorAndCursorInfo(ugcDocumentList, id2UserInfoMap);
        // 4. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcDOList);
        return ugcDOList;
    }

    void fillCurrUserAsAuthorInfo(UgcDO ugcDO) {
        UserDO author = userService.getCurrentUserInfo();
        ugcDO.setAuthor(author);
    }

    void checkSelfAuthor(UgcDO ugcDO, UgcDocument ugcDocument) {
        String authorId = ugcDocument.getAuthorId();
        UserDO author = ugcDO.getAuthor();
        if (authorId.equals(author.getUserId())) {
            return;
        }
        throw AppBizException.of(OPERATION_DENIED, "无权修改！");
    }

    void checkStatusValidation(UgcDO ugcDO, UgcDocument ugcDocument) {
        UgcStatusType updateStatus = ugcDO.getStatus();
        String statusFromDB = ugcDocument.getStatus();

        // 设置 PRIVATE 必须是 PUBLISHED
        if (updateStatus == UgcStatusType.PRIVATE && !UgcStatusType.PUBLISHED.name().equals(statusFromDB)) {
            throw AppBizException.of(OPERATION_DENIED, "非审核通过的稿件无法设置私密！");
        }
        // 设置 PUBLISHED 必须是 PRIVATE
        if (updateStatus == UgcStatusType.PUBLISHED && !UgcStatusType.PRIVATE.name().equals(statusFromDB)) {
            throw AppBizException.of(OPERATION_DENIED, "非私密稿件无法设置为公开！");
        }
    }

    void filterNoNeedInfoForListPage(List<UgcDO> ugcDOList) {
        ugcDOList.forEach(ugcDO -> {
            // 列表页无需返回 content
            ugcDO.setContent(null);
        });
    }
}
