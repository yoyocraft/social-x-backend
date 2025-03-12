package com.youyi.domain.ugc.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.domain.notification.core.NotificationManager;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.repository.CommentaryRelationshipRepository;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.relation.CommentaryNode;
import com.youyi.domain.user.model.UserDO;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.RepositoryConstant.INIT_QUERY_CURSOR;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Component
@RequiredArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final CommentaryRelationshipRepository commentaryRelationshipRepository;

    private final UgcStatisticCacheManager ugcStatisticCacheManager;
    private final NotificationManager notificationManager;

    public List<CommentaryDocument> listRootCommentary(CommentaryDO commentaryDO) {
        long cursor = getTimeCursor(commentaryDO);
        return commentaryRepository.queryRootCommentaryWithTimeCursor(
            commentaryDO.getUgcId(),
            cursor,
            commentaryDO.getSize()
        );
    }

    public List<CommentaryDocument> queryByParentId(List<CommentaryDocument> commentaryDocuments) {
        if (CollectionUtils.isEmpty(commentaryDocuments)) {
            return List.of();
        }

        Set<String> parentCommentaryIds = commentaryDocuments.stream().map(CommentaryDocument::getCommentaryId).collect(Collectors.toSet());
        return commentaryRepository.queryByParentId(parentCommentaryIds);
    }

    public List<CommentaryDO> fillCommentatorAndCursorInfo(List<CommentaryDocument> commentaryDocuments, Map<String, UserDO> id2CommentatorMap) {
        // 下一次查询的 cursor
        final String nextCursor = Optional.ofNullable(
            CollectionUtils.isEmpty(commentaryDocuments)
                ? null
                : commentaryDocuments.get(commentaryDocuments.size() - 1).getCommentaryId()
        ).orElse(SymbolConstant.EMPTY);
        return commentaryDocuments.stream()
            .map(commentaryDocument -> {
                String commentatorId = commentaryDocument.getCommentatorId();
                UserDO commentator = id2CommentatorMap.get(commentatorId);
                CommentaryDO commentaryDO = new CommentaryDO();
                commentaryDO.fillWithCommentaryDocument(commentaryDocument);
                commentaryDO.setCommentator(commentator);
                commentaryDO.setCursor(nextCursor);
                return commentaryDO;
            }).toList();
    }

    public void fillCommentaryStatistic(List<CommentaryDO> commentaryDOList) {
        if (CollectionUtils.isEmpty(commentaryDOList)) {
            return;
        }
        List<String> commentaryIds = commentaryDOList.stream().map(CommentaryDO::getCommentaryId).toList();
        Map<String, Long> commentaryStatistic = ugcStatisticCacheManager.getBatchCommentaryStatistic(commentaryIds);
        commentaryDOList.forEach(commentaryDO -> {
            Long likeCount = commentaryStatistic.getOrDefault(commentaryDO.getCommentaryId(), 0L);
            commentaryDO.calLikeCount(likeCount);
        });
    }

    private long getTimeCursor(CommentaryDO commentaryDO) {
        long cursor;
        if (INIT_QUERY_CURSOR.equals(commentaryDO.getCursor())) {
            cursor = System.currentTimeMillis();
        } else {
            CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryDO.getCursor());
            checkNotNull(commentaryDocument);
            cursor = commentaryDocument.getGmtModified();
        }
        return cursor;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCommentary(CommentaryDO commentaryDO) {
        // 查询子评论
        List<CommentaryDocument> childCommentaryDocuments = commentaryRepository.queryByParentId(commentaryDO.getCommentaryId());
        // 删除子评论
        List<String> childCommentaryIds = childCommentaryDocuments.stream().map(CommentaryDocument::getCommentaryId).toList();
        if (CollectionUtils.isNotEmpty(childCommentaryIds)) {
            commentaryRepository.batchDeleteCommentary(childCommentaryIds);
        }
        // 删除父评论
        commentaryRepository.deleteCommentary(commentaryDO.getCommentaryId());
    }

    public void likeCommentary(CommentaryDO commentaryDO, UserDO currentUser) {
        // 如果需要创建评论节点
        createCommentaryIfNeed(commentaryDO);

        // 创建喜欢关系
        commentaryRelationshipRepository.addLikeRelationship(commentaryDO.getCommentaryId(), currentUser.getUserId());
        // 增加点赞数
        ugcStatisticCacheManager.incrOrDecrCommentaryLikeCount(commentaryDO.getCommentaryId(), true);
    }

    public void cancelLikeCommentary(CommentaryDO commentaryDO, UserDO currentUser) {
        // 无需创建节点
        // 删除喜欢关系
        commentaryRelationshipRepository.deleteLikeRelationship(commentaryDO.getCommentaryId(), currentUser.getUserId());
        // 减少点赞数
        ugcStatisticCacheManager.incrOrDecrCommentaryLikeCount(commentaryDO.getCommentaryId(), false);
    }

    public void createCommentaryIfNeed(CommentaryDO commentaryDO) {
        Optional<CommentaryNode> commentaryNodeOptional = Optional.ofNullable(commentaryRelationshipRepository.findByCommentaryId(commentaryDO.getCommentaryId()));
        if (commentaryNodeOptional.isPresent()) {
            return;
        }
        commentaryRelationshipRepository.save(commentaryDO.getCommentaryId());
    }

    public void sendNotification(CommentaryDO commentaryDO) {
        if (commentaryDO.isTopCommentary()) {
            // 发送UGC_COMMENT通知
            notificationManager.sendUgcCommentNotification(commentaryDO.getCommentator(), commentaryDO.getCommentaryId(), commentaryDO.getCommentary(), commentaryDO.getUgcId());
            return;
        }

        // 发送UGC_COMMENT_REPLY通知
        notificationManager.sendUgcCommentReplyNotification(commentaryDO.getCommentator(), commentaryDO.getParentId(), commentaryDO.getCommentary(), commentaryDO.getUgcId());
    }

    public void fillInteractInfo(List<CommentaryDO> commentaryDOList, UserDO currentUserInfo) {
        List<String> commentaryIds = commentaryDOList.stream()
            .map(CommentaryDO::getCommentaryId)
            .collect(Collectors.toList());

        // 批量查询用户对多个评论的点赞关系，返回已点赞的评论ID列表
        List<String> likedCommentaryIds = commentaryRelationshipRepository.queryLikeRelationships(commentaryIds, currentUserInfo.getUserId());

        Set<String> likedCommentaryIdSet = new HashSet<>(likedCommentaryIds);

        // 设置每个评论的点赞状态
        commentaryDOList.forEach(commentaryDO -> {
            commentaryDO.setLiked(likedCommentaryIdSet.contains(commentaryDO.getCommentaryId()));
        });
    }
}
