package com.youyi.domain.ugc.core;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;
import static com.youyi.domain.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;

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

    public Page<UgcDO> querySelfUgc(UgcDO ugcDO) {
        UserDO author = ugcDO.getAuthor();

        Page<UgcDocument> documents = ugcRepository.queryByKeywordAndStatusForSelf(
            ugcDO.getKeyword(),
            ugcDO.getStatus().name(),
            author.getUserId(),
            ugcDO.getPage(),
            ugcDO.getSize()
        );

        List<UgcDO> ugcInfoList = documents.getContent()
            .stream()
            .map(document -> {
                UgcDO ugcInfo = UGC_ASSEMBLER.toDO(document);
                ugcInfo.setAuthor(author);
                return ugcInfo;
            }).toList();

        return new PageImpl<>(ugcInfoList, documents.getPageable(), documents.getTotalElements());
    }

    void checkStatusValidationBeforeUpdate(UgcDocument ugcDocument) {
        // 私密的稿件禁止修改
        if (UgcStatusType.PRIVATE.name().equals(ugcDocument.getStatus())) {
            throw AppBizException.of(OPERATION_DENIED, "私密稿件禁止修改！");
        }
    }
}
