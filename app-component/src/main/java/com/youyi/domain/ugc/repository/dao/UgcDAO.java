package com.youyi.domain.ugc.repository.dao;

import com.mongodb.client.result.UpdateResult;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.RepositoryConstant.MONGO_IGNORE_CASE_OPTION;
import static com.youyi.common.constant.RepositoryConstant.ofFuzzyQuery;
import static com.youyi.common.constant.UgcConstant.UGC_ATTACHMENT_URLS;
import static com.youyi.common.constant.UgcConstant.UGC_AUTHOR_ID;
import static com.youyi.common.constant.UgcConstant.UGC_CATEGORY_ID;
import static com.youyi.common.constant.UgcConstant.UGC_CONTENT;
import static com.youyi.common.constant.UgcConstant.UGC_COVER;
import static com.youyi.common.constant.UgcConstant.UGC_EXTRA_DATA;
import static com.youyi.common.constant.UgcConstant.UGC_GMT_MODIFIED;
import static com.youyi.common.constant.UgcConstant.UGC_ID;
import static com.youyi.common.constant.UgcConstant.UGC_STATUS;
import static com.youyi.common.constant.UgcConstant.UGC_SUMMARY;
import static com.youyi.common.constant.UgcConstant.UGC_TAGS;
import static com.youyi.common.constant.UgcConstant.UGC_TITLE;
import static com.youyi.common.constant.UgcConstant.UGC_TYPE;
import static com.youyi.common.constant.UgcConstant.excludeStatus;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Component
@RequiredArgsConstructor
public class UgcDAO {

    private final MongoTemplate mongoTemplate;

    public void save(UgcDocument ugcDocument) {
        mongoTemplate.save(ugcDocument);
    }

    public UpdateResult updateByUgcId(UgcDocument ugcDocument) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_ID).is(ugcDocument.getUgcId()));

        Update updateDef = new Update()
            .set(UGC_TITLE, ugcDocument.getTitle())
            .set(UGC_CONTENT, ugcDocument.getContent())
            .set(UGC_SUMMARY, ugcDocument.getSummary())
            .set(UGC_COVER, ugcDocument.getCover())
            .set(UGC_ATTACHMENT_URLS, ugcDocument.getAttachmentUrls())
            .set(UGC_TAGS, ugcDocument.getTags())
            .set(UGC_STATUS, ugcDocument.getStatus())
            .set(UGC_GMT_MODIFIED, ugcDocument.getGmtModified())
            .set(UGC_EXTRA_DATA, ugcDocument.getExtraData());
        return mongoTemplate.updateFirst(query, updateDef, UgcDocument.class);
    }

    public UpdateResult updateStatusByUgcId(String ugcId, String ugcStatus) {
        Query query = new Query(Criteria.where(UGC_ID).is(ugcId));
        Update updateDef = new Update()
            .set(UGC_STATUS, ugcStatus)
            .set(UGC_GMT_MODIFIED, LocalDateTime.now());
        return mongoTemplate.updateFirst(query, updateDef, UgcDocument.class);
    }

    public UgcDocument queryByUgcId(String ugcId) {
        Query query = new Query(Criteria.where(UGC_ID).is(ugcId));
        return mongoTemplate.findOne(query, UgcDocument.class);
    }

    public List<UgcDocument> queryByKeywordAndStatusForSelfWithCursor(String keyword, String ugcStatus, String authorId,
        LocalDateTime lastCursor, int size) {

        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_AUTHOR_ID).is(authorId));
        buildUgcStatusQueryCondition(ugcStatus, query);
        buildKeywordQueryCondition(keyword, query);
        buildTimeCursorQueryCondition(lastCursor, size, query);
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryByStatusWithTimeCursor(String ugcStatus, LocalDateTime lastCursor, int size) {
        Query query = new Query();
        buildUgcStatusQueryCondition(ugcStatus, query);
        buildTimeCursorQueryCondition(lastCursor, size, query);
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryMainPageInfoWithIdCursor(String categoryId, String type, String ugcStatus, LocalDateTime lastCursor, int size) {
        Query query = new Query();
        buildUgcStatusQueryCondition(ugcStatus, query);
        if (StringUtils.isNotBlank(type)) {
            query.addCriteria(Criteria.where(UGC_TYPE).is(type));
        }
        if (StringUtils.isNotBlank(categoryId)) {
            query.addCriteria(Criteria.where(UGC_CATEGORY_ID).is(categoryId));
        }
        buildTimeCursorQueryCondition(lastCursor, size, query);

        return mongoTemplate.find(query, UgcDocument.class);
    }

    private static void buildUgcStatusQueryCondition(String ugcStatus, Query query) {
        if (UgcStatusType.ALL != UgcStatusType.of(ugcStatus)) {
            // 查询指定状态的 UGC
            query.addCriteria(Criteria.where(UGC_STATUS).is(ugcStatus));
        } else {
            // 查询除 DRAFT, DELETED 的 UGC
            query.addCriteria(Criteria.where(UGC_STATUS).nin(excludeStatus));
        }
    }

    private static void buildKeywordQueryCondition(String keyword, Query query) {
        if (StringUtils.isBlank(keyword)) {
            return;
        }
        Criteria regexCriteria = new Criteria().orOperator(
            Criteria.where(UGC_TITLE).regex(ofFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION),
            Criteria.where(UGC_SUMMARY).regex(ofFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION)
        );
        query.addCriteria(regexCriteria);

    }

    private static void buildTimeCursorQueryCondition(LocalDateTime lastCursor, int size, Query query) {
        // 时间倒序查询
        query.addCriteria(Criteria.where(UGC_GMT_MODIFIED).lt(lastCursor));
        query.limit(size);
        query.with(Sort.by(Sort.Direction.DESC, UGC_GMT_MODIFIED, UGC_ID));
    }
}
