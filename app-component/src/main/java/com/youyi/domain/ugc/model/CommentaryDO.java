package com.youyi.domain.ugc.model;

import com.youyi.domain.ugc.type.CommentaryStatus;
import com.youyi.domain.ugc.type.UgcInteractionType;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
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

    private List<String> attachmentUrls;

    private Long gmtCreate;
    private Long gmtModified;

    private Boolean liked;

    /**
     * for query
     */
    private String cursor;
    private int size;

    // for interact
    private Boolean interactFlag;
    private UgcInteractionType interactionType;

    public void create() {
        this.commentaryId = IdSeqUtil.genCommentaryId();
        this.likeCount = 0L;
        this.status = CommentaryStatus.NORMAL;
        if (StringUtils.isBlank(parentId)) {
            this.parentId = TOP_COMMENTARY_ID;
        }
        this.gmtCreate = System.currentTimeMillis();
        this.gmtModified = System.currentTimeMillis();
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
        commentaryDocument.setAttachmentUrls(attachmentUrls);
        commentaryDocument.setExtraData(extraData);
        commentaryDocument.setGmtCreate(gmtCreate);
        commentaryDocument.setGmtModified(gmtModified);
        return commentaryDocument;
    }

    public void fillWithCommentaryDocument(CommentaryDocument document) {
        this.commentaryId = document.getCommentaryId();
        this.parentId = document.getParentId();
        this.ugcId = document.getUgcId();
        this.commentary = document.getCommentary();
        this.likeCount = document.getLikeCount();
        this.status = CommentaryStatus.of(document.getStatus());
        this.attachmentUrls = document.getAttachmentUrls();
        this.extraData = document.getExtraData();
        this.gmtCreate = document.getGmtCreate();
        this.gmtModified = document.getGmtModified();
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
