package com.youyi.domain.ugc.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

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

    public List<CommentaryDocument> queryByUgcIdWithTimeCursor(CommentaryDO commentaryDO) {
        long cursor = getTimeCursor(commentaryDO);
        return commentaryRepository.queryByUgcIdWithTimeCursor(
            commentaryDO.getUgcId(),
            cursor,
            commentaryDO.getSize()
        );
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

    public void deleteCommentary(CommentaryDO commentaryDO) {
        // 查询子评论
        List<CommentaryDocument> childCommentaryDocuments = commentaryRepository.queryByParentId(commentaryDO.getCommentaryId());
        // TODO youyi 2025/1/28 保证事务和数据一致性
        // 删除子评论
        List<String> childCommentaryIds = childCommentaryDocuments.stream().map(CommentaryDocument::getCommentaryId).toList();
        if (CollectionUtils.isNotEmpty(childCommentaryIds)) {
            commentaryRepository.batchDeleteCommentary(childCommentaryIds);
        }
        // 删除父评论
        commentaryRepository.deleteCommentary(commentaryDO.getCommentaryId());
    }
}
