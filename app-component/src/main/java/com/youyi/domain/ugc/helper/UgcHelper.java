package com.youyi.domain.ugc.helper;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.core.UgcService;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public Page<UgcDO> querySelfUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        return ugcService.querySelfUgc(ugcDO);
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
}
