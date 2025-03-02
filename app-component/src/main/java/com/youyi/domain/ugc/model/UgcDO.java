package com.youyi.domain.ugc.model;

import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
@Setter
public class UgcDO {

    private String ugcId;
    private UgcType ugcType;
    private UserDO author;
    private String title;
    private String content;
    private String summary;
    private String categoryId;
    private String categoryName;
    private List<String> tags;

    private Long viewCount;
    private Long likeCount;
    private Long collectCount;
    private Long commentaryCount;

    private UgcStatus status;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 附件地址（图片），动态贴图
     */
    private List<String> attachmentUrls;

    private UgcExtraData extraData;

    private Long gmtCreate;
    private Long gmtModified;

    // for page query
    private int page;
    private int size;
    private String keyword;
    private String cursor;
    private String authorId;
    private Boolean qaStatus;
    private Long timeCursor;

    // for like and collect
    private UgcInteractionType interactionType;
    private Boolean interactFlag;
    private Boolean liked;
    private Boolean collected;

    // for hot
    private Double hotScore;

    public void create() {
        this.ugcId = IdSeqUtil.genUgcId();
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.collectCount = 0L;
        this.commentaryCount = 0L;
        this.extraData = new UgcExtraData();
    }

    public UgcDocument buildToSaveUgcDocument() {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(ugcId);
        ugcDocument.setType(ugcType.name());
        ugcDocument.setAuthorId(author.getUserId());
        ugcDocument.setTitle(title);
        ugcDocument.setContent(content);
        ugcDocument.setSummary(summary);
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setTags(tags);
        ugcDocument.setViewCount(viewCount);
        ugcDocument.setLikeCount(likeCount);
        ugcDocument.setCollectCount(collectCount);
        ugcDocument.setCommentaryCount(commentaryCount);
        ugcDocument.setStatus(status.name());
        ugcDocument.setCover(cover);
        ugcDocument.setAttachmentUrls(attachmentUrls);
        ugcDocument.setGmtCreate(System.currentTimeMillis());
        ugcDocument.setGmtModified(System.currentTimeMillis());
        ugcDocument.setExtraData(extraData);
        return ugcDocument;
    }

    public boolean isNew() {
        return StringUtils.isBlank(ugcId);
    }

    public void fillBeforeUpdateWhenPublish(UgcDocument ugcDocument) {
        this.gmtCreate = ugcDocument.getGmtCreate();
    }

    public UgcDocument buildToUpdateUgcDocumentWhenPublish() {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(ugcId);
        ugcDocument.setTitle(title);
        ugcDocument.setContent(content);
        ugcDocument.setSummary(summary);
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setTags(tags);
        ugcDocument.setStatus(status.name());
        ugcDocument.setCover(cover);
        ugcDocument.setAttachmentUrls(attachmentUrls);
        ugcDocument.setGmtCreate(gmtCreate);
        ugcDocument.setGmtModified(System.currentTimeMillis());
        return ugcDocument;
    }

    public void fillWithUgcDocument(UgcDocument ugcDocument) {
        this.ugcId = ugcDocument.getUgcId();
        this.ugcType = UgcType.of(ugcDocument.getType());
        this.title = ugcDocument.getTitle();
        this.content = ugcDocument.getContent();
        this.summary = ugcDocument.getSummary();
        this.categoryId = ugcDocument.getCategoryId();
        this.categoryName = ugcDocument.getCategoryName();
        this.tags = ugcDocument.getTags();
        this.viewCount = ugcDocument.getViewCount();
        this.likeCount = ugcDocument.getLikeCount();
        this.collectCount = ugcDocument.getCollectCount();
        this.commentaryCount = ugcDocument.getCommentaryCount();
        this.status = UgcStatus.of(ugcDocument.getStatus());
        this.cover = ugcDocument.getCover();
        this.attachmentUrls = ugcDocument.getAttachmentUrls();
        this.extraData = ugcDocument.getExtraData();
        this.gmtCreate = ugcDocument.getGmtCreate();
        this.gmtModified = ugcDocument.getGmtModified();
    }

    public void calViewCount(Long toAddCount) {
        if (Objects.isNull(toAddCount) || toAddCount <= 0) {
            return;
        }
        this.viewCount = this.viewCount + toAddCount;
    }

    public void calLikeCount(Long toAddCount) {
        if (Objects.isNull(toAddCount) || toAddCount <= 0) {
            return;
        }
        this.likeCount = this.likeCount + toAddCount;
    }

    public void calCollectCount(Long toAddCount) {
        if (Objects.isNull(toAddCount) || toAddCount <= 0) {
            return;
        }
        this.collectCount = this.collectCount + toAddCount;
    }

    public void calCommentaryCount(Long toAddCount) {
        if (Objects.isNull(toAddCount) || toAddCount <= 0) {
            return;
        }
        this.commentaryCount = this.commentaryCount + toAddCount;
    }
}
