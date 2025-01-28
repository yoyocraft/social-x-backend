package com.youyi.domain.ugc.helper;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.ugc.core.CommentaryService;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.model.CommentaryExtraData;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Service
@RequiredArgsConstructor
public class CommentaryHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentaryHelper.class);

    private final UserService userService;
    private final CommentaryService commentaryService;
    private final CommentaryRepository commentaryRepository;

    public void publish(CommentaryDO commentaryDO) {
        fillCurrUserAsCommentator(commentaryDO);
        commentaryDO.create();
        sensitiveHandle(commentaryDO);
        commentaryRepository.saveCommentary(commentaryDO.buildToSaveCommentaryDocument());
    }

    public List<CommentaryDO> queryUgcCommentary(CommentaryDO commentaryDO) {
        List<CommentaryDocument> commentaryDocumentList = commentaryService.queryByUgcIdWithTimeCursor(commentaryDO);
        Set<String> commentatorIds = commentaryDocumentList.stream().map(CommentaryDocument::getCommentatorId).collect(Collectors.toSet());
        Map<String, UserDO> id2CommentatorMap = userService.queryBatchByUserId(commentatorIds);
        return commentaryService.fillCommentatorAndCursorInfo(commentaryDocumentList, id2CommentatorMap);
    }

    public void deleteCommentary(CommentaryDO commentaryDO) {
        fillCurrUserAsCommentator(commentaryDO);
        CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryDO.getCommentaryId());
        checkSelfCommentator(commentaryDO, commentaryDocument);
        commentaryService.deleteCommentary(commentaryDO);
    }

    private void fillCurrUserAsCommentator(CommentaryDO commentaryDO) {
        UserDO commentator = userService.getCurrentUserInfo();
        commentaryDO.setCommentator(commentator);
    }

    private void sensitiveHandle(CommentaryDO commentaryDO) {
        CommentaryExtraData extraData = Optional.ofNullable(commentaryDO.getExtraData()).orElseGet(CommentaryExtraData::new);
        // 检测敏感词
        boolean isSensitive = SensitiveWordHelper.contains(commentaryDO.getCommentary());
        extraData.setSensitive(isSensitive);
        if (isSensitive) {
            LOGGER.warn("user:{} publish sensitive commentary:{}", commentaryDO.getCommentator().getUserId(), commentaryDO.getCommentaryId());
            // 替换敏感词
            String nonSensitiveCommentary = SensitiveWordHelper.replace(commentaryDO.getCommentary());
            commentaryDO.setCommentary(nonSensitiveCommentary);
            // 设置状态为敏感
            commentaryDO.setStatus(CommentaryStatus.SENSITIVE);
        }
    }

    private void checkSelfCommentator(CommentaryDO commentaryDO, CommentaryDocument commentaryDocument) {
        String commentatorId = commentaryDocument.getCommentatorId();
        UserDO commentator = commentaryDO.getCommentator();
        if (commentatorId.equals(commentator.getUserId())) {
            return;
        }
        throw AppBizException.of(OPERATION_DENIED, "无权修改！");
    }
}
