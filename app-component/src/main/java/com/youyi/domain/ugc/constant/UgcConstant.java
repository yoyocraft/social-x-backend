package com.youyi.domain.ugc.constant;

import com.youyi.domain.ugc.type.CommentaryStatus;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcType;
import java.util.EnumSet;
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
    public static final String UGC_CATEGORY_NAME = "categoryName";
    public static final String UGC_VIEW_COUNT = "viewCount";
    public static final String UGC_LIKE_COUNT = "likeCount";
    public static final String UGC_COLLECT_COUNT = "collectCount";
    public static final String UGC_COMMENTARY_COUNT = "commentaryCount";
    public static final String UGC_QUESTION_HAS_SOLVED = "extraData.hasSolved";

    public static List<String> includeUgcStatus = List.of(
        UgcStatus.PUBLISHED.name(),
        UgcStatus.AUDITING.name()
    );

    public static EnumSet<UgcType> hotUgcTypeList = EnumSet.of(
        UgcType.ARTICLE,
        UgcType.POST,
        UgcType.QUESTION,
        UgcType.ALL
    );

    // ======================= Commentary =======================
    public static final String COMMENTARY_ID = "commentaryId";
    public static final String COMMENTARY_PARENT_ID = "parentId";
    public static final String COMMENTARY_UGC_ID = "ugcId";
    public static final String COMMENTARY_COMMENTATOR_ID = "commentatorId";
    public static final String COMMENTARY_LIKE_COUNT = "likeCount";
    public static final String COMMENTARY_STATUS = "status";
    public static final String COMMENTARY_EXTRA_DATA = "extraData";
    public static final String COMMENTARY_GMT_MODIFIED = "gmtModified";

    public static List<String> includeCommentaryStatus = List.of(
        CommentaryStatus.NORMAL.name(),
        CommentaryStatus.SENSITIVE.name()
    );

    public static final String AUDIT_PASS = "审核通过";

}
