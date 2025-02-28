package com.youyi.domain.ugc.repository.document;

import com.youyi.common.base.BaseDocument;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.ugc.model.CommentaryExtraData;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Getter
@Setter
@Document(value = "commentary")
public class CommentaryDocument extends BaseDocument {

    private String commentaryId;

    private String parentId;

    private String ugcId;

    private String commentatorId;

    private String commentary;

    private Long likeCount;

    /**
     * @see CommentaryStatus
     */
    private String status;

    private List<String> attachmentUrls;

    private CommentaryExtraData extraData;
}
