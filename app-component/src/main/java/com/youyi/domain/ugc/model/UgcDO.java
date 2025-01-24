package com.youyi.domain.ugc.model;

import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.RandomGenUtil;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.time.LocalDateTime;
import java.util.List;
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
    private List<String> tags;

    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    private UgcStatusType status;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 附件地址（图片），动态贴图
     */
    private List<String> attachmentUrls;

    private UgcExtraData extraData;

    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    // for page query
    private int page;
    private int size;
    private String keyword;

    public void create() {
        this.ugcId = RandomGenUtil.genUgcId();
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public UgcDocument buildToSaveUgcDocument() {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(ugcId);
        ugcDocument.setType(ugcType.name());
        ugcDocument.setAuthorId(author.getUserId());
        ugcDocument.setTitle(title);
        ugcDocument.setContent(content);
        ugcDocument.setSummary(summary);
        ugcDocument.setTags(tags);
        ugcDocument.setViewCount(viewCount);
        ugcDocument.setLikeCount(likeCount);
        ugcDocument.setCommentCount(commentCount);
        ugcDocument.setStatus(status.name());
        ugcDocument.setCover(cover);
        ugcDocument.setAttachmentUrls(attachmentUrls);
        ugcDocument.setGmtCreate(LocalDateTime.now());
        ugcDocument.setGmtModified(LocalDateTime.now());
        ugcDocument.setExtraData(GsonUtil.toJson(extraData));
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
        ugcDocument.setTags(tags);
        ugcDocument.setStatus(status.name());
        ugcDocument.setCover(cover);
        ugcDocument.setAttachmentUrls(attachmentUrls);
        ugcDocument.setGmtCreate(gmtCreate);
        ugcDocument.setGmtModified(LocalDateTime.now());
        return ugcDocument;
    }
}
