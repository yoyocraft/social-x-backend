package com.youyi.common.constant;

import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.common.type.ugc.UgcStatus;
import java.util.List;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcConstant {
    // ======================= UGC =======================
    public static final String UGC_ID = "ugcId";
    public static final String UGC_TYPE = "type";
    public static final String UGC_AUTHOR_ID = "authorId";
    public static final String UGC_STATUS = "status";
    public static final String UGC_GMT_MODIFIED = "gmtModified";
    public static final String UGC_EXTRA_DATA = "extraData";
    public static final String UGC_TITLE = "title";
    public static final String UGC_CONTENT = "content";
    public static final String UGC_SUMMARY = "summary";
    public static final String UGC_TAGS = "tags";
    public static final String UGC_COVER = "cover";
    public static final String UGC_ATTACHMENT_URLS = "attachmentUrls";
    public static final String UGC_CATEGORY_ID = "categoryId";
    public static final String UGC_VIEW_COUNT = "viewCount";
    public static final String UGC_LIKE_COUNT = "likeCount";
    public static final String UGC_COLLECT_COUNT = "collectCount";
    public static final String UGC_COMMENTARY_COUNT = "commentaryCount";
    public static final String UGC_QUESTION_HAS_SOLVED = "extraData.hasSolved";

    public static List<String> includeUgcStatus = List.of(
        UgcStatus.PUBLISHED.name(),
        UgcStatus.AUDITING.name(),
        UgcStatus.PRIVATE.name()
    );

    // ======================= Commentary =======================
    public static final String COMMENTARY_ID = "commentaryId";
    public static final String COMMENTARY_PARENT_ID = "parentId";
    public static final String COMMENTARY_UGC_ID = "ugcId";
    public static final String COMMENTARY_LIKE_COUNT = "likeCount";
    public static final String COMMENTARY_STATUS = "status";
    public static final String COMMENTARY_EXTRA_DATA = "extraData";
    public static final String COMMENTARY_GMT_MODIFIED = "gmtModified";

    public static List<String> includeCommentaryStatus = List.of(
        CommentaryStatus.NORMAL.name(),
        CommentaryStatus.SENSITIVE.name()
    );

}
