package com.youyi.domain.ugc.repository.document;

import com.youyi.common.base.BaseDocument;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.domain.ugc.model.UgcExtraData;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
@Setter
@Document(value = "ugc")
public class UgcDocument extends BaseDocument {

    @Id
    private String id;

    private String ugcId;

    /**
     * @see UgcType
     */
    private String type;

    private String authorId;

    private String title;

    private String content;

    private String summary;

    private String categoryId;

    private String categoryName;

    private List<String> tags;

    private Long viewCount;

    private Long likeCount;

    private Long commentCount;

    /**
     * @see UgcStatus
     */
    private String status;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 附件地址（图片），动态贴图
     */
    private List<String> attachmentUrls;

    private UgcExtraData extraData;
}
