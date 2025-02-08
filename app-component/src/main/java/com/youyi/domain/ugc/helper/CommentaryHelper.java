package com.youyi.domain.ugc.helper;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.task.TaskType;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.task.core.SysTaskService;
import com.youyi.domain.ugc.core.CommentaryService;
import com.youyi.domain.ugc.core.UgcTpeContainer;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.model.CommentaryExtraData;
import com.youyi.domain.ugc.repository.CommentaryRelationshipRepository;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
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

    private final UgcTpeContainer ugcTpeContainer;
    private final SysTaskService sysTaskService;

    private final UserService userService;
    private final CommentaryService commentaryService;
    private final CommentaryRepository commentaryRepository;
    private final CommentaryRelationshipRepository commentaryRelationshipRepository;

    public void publish(CommentaryDO commentaryDO) {
        // 0. 填充当前用户信息作为评论者
        fillCurrUserAsCommentator(commentaryDO);
        // 1. 创建评论
        commentaryDO.create();
        // 2. 敏感词处理
        sensitiveHandle(commentaryDO);
        // 3. 存储到数据库中
        commentaryRepository.saveCommentary(commentaryDO.buildToSaveCommentaryDocument());
        // 4. 发送通知
        commentaryService.sendNotification(commentaryDO);
    }

    public List<CommentaryDO> queryUgcCommentary(CommentaryDO commentaryDO) {
        List<CommentaryDocument> commentaryDocumentList = commentaryService.queryByUgcIdWithTimeCursor(commentaryDO);
        Set<String> commentatorIds = commentaryDocumentList.stream().map(CommentaryDocument::getCommentatorId).collect(Collectors.toSet());
        Map<String, UserDO> id2CommentatorMap = userService.queryBatchByUserId(commentatorIds);
        List<CommentaryDO> commentaryDOList = commentaryService.fillCommentatorAndCursorInfo(commentaryDocumentList, id2CommentatorMap);
        // 填充 Statistic 数据
        commentaryService.fillCommentaryStatistic(commentaryDOList);
        return commentaryDOList;
    }

    public void deleteCommentary(CommentaryDO commentaryDO) {
        fillCurrUserAsCommentator(commentaryDO);
        CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryDO.getCommentaryId());
        checkSelfCommentator(commentaryDO, commentaryDocument);
        commentaryService.deleteCommentary(commentaryDO);
        // 异步写入本地消息
        ugcTpeContainer.getUgcDeleteTaskExecutor().execute(() -> sysTaskService.saveUgcDeleteTask(commentaryDO.getCommentaryId(), TaskType.COMMENTARY_DELETE_EVENT));
    }

    public void like(CommentaryDO commentaryDO) {
        UserDO currentUser = userService.getCurrentUserInfo();
        if (Boolean.TRUE.equals(commentaryDO.getInteractFlag())) {
            doLike(commentaryDO, currentUser);
            return;
        }

        doCancelLike(commentaryDO, currentUser);
    }

    private void doLike(CommentaryDO commentaryDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasLikeOptional = Optional.ofNullable(commentaryRelationshipRepository.queryLikeRelationship(commentaryDO.getCommentaryId(), currentUser.getUserId()));
        if (hasLikeOptional.isPresent()) {
            return;
        }
        // 创建用户节点信息
        userService.createUserIfNeed(currentUser);
        // 添加喜欢关系
        commentaryService.likeCommentary(commentaryDO, currentUser);
    }

    private void doCancelLike(CommentaryDO commentaryDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasLikeOptional = Optional.ofNullable(commentaryRelationshipRepository.queryLikeRelationship(commentaryDO.getCommentaryId(), currentUser.getUserId()));
        if (hasLikeOptional.isEmpty()) {
            return;
        }
        // 删除喜欢关系
        commentaryService.cancelLikeCommentary(commentaryDO, currentUser);
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
