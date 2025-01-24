package com.youyi.domain.ugc.helper;

import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import static com.youyi.domain.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Service
@RequiredArgsConstructor
public class UgcHelper {

    private final UserService userService;
    private final UgcRepository ugcRepository;

    public void publishUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        ugcDO.create();
        ugcRepository.saveUgc(ugcDO.buildToSaveUgcDocument());
    }

    public Page<UgcDO> querySelfUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
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

    void fillCurrUserAsAuthorInfo(UgcDO ugcDO) {
        UserDO author = userService.getCurrentUserInfo();
        ugcDO.setAuthor(author);
    }
}
