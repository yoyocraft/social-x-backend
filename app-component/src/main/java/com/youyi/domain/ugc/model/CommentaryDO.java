package com.youyi.domain.ugc.model;

import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.youyi.common.constant.RepositoryConstant.TOP_COMMENTARY_ID;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryDO {

    private String commentaryId;
    private String parentId;
    private String ugcId;
    private UserDO commentator;
    private String commentary;

    private Long likeCount;
    private CommentaryStatus status;

    private CommentaryExtraData extraData;

    private Long gmtCreate;
    private Long gmtModified;

    /**
     * for query
     */
    private String cursor;
    private int size;

    // for interact
    private Boolean interactFlag;

    public void create() {
        this.commentaryId = IdSeqUtil.genCommentaryId();
        this.likeCount = 0L;
        this.status = CommentaryStatus.NORMAL;
        if (StringUtils.isBlank(parentId)) {
            this.parentId = TOP_COMMENTARY_ID;
        }
    }

    public CommentaryDocument buildToSaveCommentaryDocument() {
        CommentaryDocument commentaryDocument = new CommentaryDocument();
        commentaryDocument.setCommentaryId(commentaryId);
        commentaryDocument.setParentId(parentId);
        commentaryDocument.setUgcId(ugcId);
        commentaryDocument.setCommentatorId(commentator.getUserId());
        commentaryDocument.setCommentary(commentary);
        commentaryDocument.setLikeCount(likeCount);
        commentaryDocument.setStatus(status.name());
        commentaryDocument.setExtraData(extraData);
        commentaryDocument.setGmtCreate(System.currentTimeMillis());
        commentaryDocument.setGmtModified(System.currentTimeMillis());
        return commentaryDocument;
    }

    public void fillWithCommentaryDocument(CommentaryDocument document) {
        this.commentaryId = document.getCommentaryId();
        this.parentId = document.getParentId();
        this.ugcId = document.getUgcId();
        this.commentary = document.getCommentary();
        this.likeCount = document.getLikeCount();
        this.status = CommentaryStatus.of(document.getStatus());
        this.extraData = document.getExtraData();
    }

    public void calLikeCount(Long toAddCount) {
        if (Objects.isNull(toAddCount) || toAddCount <= 0) {
            return;
        }
        this.likeCount = this.likeCount + toAddCount;
    }

    public boolean isTopCommentary() {
        return TOP_COMMENTARY_ID.equals(parentId);
    }
}
