package com.youyi.common.constant;

import java.util.List;

import static com.youyi.common.type.ugc.UgcStatusType.DELETED;
import static com.youyi.common.type.ugc.UgcStatusType.DRAFT;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcConstant {

    public static final String DOCUMENT_ID = "_id";

    public static final String UGC_ID = "ugcId";

    public static final String UGC_TYPE = "type";

    public static final String UGC_AUTHOR_ID = "authorId";

    public static final String UGC_STATUS = "status";

    public static final String UGC_GMT_MODIFIED = "gmtModified";
    public static final String UGC_GMT_CREATE = "gmtCreate";
    public static final String UGC_EXTRA_DATA = "extraData";

    public static final String UGC_TITLE = "title";
    public static final String UGC_CONTENT = "content";
    public static final String UGC_SUMMARY = "summary";
    public static final String UGC_TAGS = "tags";
    public static final String UGC_COVER = "cover";
    public static final String UGC_ATTACHMENT_URLS = "attachmentUrls";

    public static final String UGC_CATEGORY_ID = "categoryId";

    public static List<String> excludeStatus = List.of(
        DRAFT.name(),
        DELETED.name()
    );

}
