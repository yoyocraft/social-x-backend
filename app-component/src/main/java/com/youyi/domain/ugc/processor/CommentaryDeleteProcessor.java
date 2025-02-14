package com.youyi.domain.ugc.processor;

import com.google.common.collect.Lists;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.task.core.TaskProcessor;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.ugc.repository.CommentaryRelationshipRepository;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class CommentaryDeleteProcessor implements TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UgcDeleteProcessor.class);

    private final CommentaryRepository commentaryRepository;
    private final CommentaryRelationshipRepository commentaryRelationshipRepository;

    @Override
    public void process(String taskId, SysTaskExtraData extraData) {
        if (Objects.isNull(extraData)) {
            logger.warn("extraData is null, taskId: {}", taskId);
            return;
        }
        logger.info("[TaskProcessor]delete commentary, taskId: {}, extraData: {}", taskId, GsonUtil.toJson(extraData));
        String commentaryId = extraData.getTargetId();
        List<String> allToDeleteCommentaryIds = Lists.newArrayList(commentaryId);
        List<CommentaryDocument> childDocuments = commentaryRepository.queryByParentId(commentaryId);
        if (CollectionUtils.isNotEmpty(childDocuments)) {
            List<String> childCommentaryIds = childDocuments.stream().map(CommentaryDocument::getCommentaryId).toList();
            allToDeleteCommentaryIds.addAll(childCommentaryIds);
            // 删除子评论的点赞关系
            commentaryRelationshipRepository.deleteAllLikeRelationships(childCommentaryIds);
        }
        // 删除父评论的点赞关系
        commentaryRelationshipRepository.deleteAllLikeRelationships(commentaryId);
        // 删除所有的评论节点
        logger.info("delete commentary nodes, ids:{}", GsonUtil.toJson(allToDeleteCommentaryIds));
        commentaryRelationshipRepository.deleteCommentaryNode(allToDeleteCommentaryIds);
    }
}
