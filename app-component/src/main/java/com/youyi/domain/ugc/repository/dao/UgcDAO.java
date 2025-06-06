package com.youyi.domain.ugc.repository.dao;

import com.mongodb.client.result.UpdateResult;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.RepositoryConstant.MONGO_IGNORE_CASE_OPTION;
import static com.youyi.common.util.CommonOperationUtil.ofMongoFuzzyQuery;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_ATTACHMENT_URLS;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_AUTHOR_ID;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_CATEGORY_ID;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_CATEGORY_NAME;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_COLLECT_COUNT;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_COMMENTARY_COUNT;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_CONTENT;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_COVER;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_EXTRA_DATA;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_GMT_MODIFIED;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_ID;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_LIKE_COUNT;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_QUESTION_HAS_SOLVED;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_STATUS;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_SUMMARY;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_TAGS;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_TITLE;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_TYPE;
import static com.youyi.domain.ugc.constant.UgcConstant.UGC_VIEW_COUNT;
import static com.youyi.domain.ugc.constant.UgcConstant.includeUgcStatus;

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

    public void saveAll(Collection<UgcDocument> ugcDocuments) {
        mongoTemplate.insertAll(ugcDocuments);
    }

    public UpdateResult updateByUgcId(UgcDocument ugcDocument) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_ID).is(ugcDocument.getUgcId()));

        Update updateDef = new Update()
            .set(UGC_TITLE, ugcDocument.getTitle())
            .set(UGC_CATEGORY_ID, ugcDocument.getCategoryId())
            .set(UGC_CATEGORY_NAME, ugcDocument.getCategoryName())
            .set(UGC_CONTENT, ugcDocument.getContent())
            .set(UGC_SUMMARY, ugcDocument.getSummary())
            .set(UGC_COVER, ugcDocument.getCover())
            .set(UGC_ATTACHMENT_URLS, ugcDocument.getAttachmentUrls())
            .set(UGC_TAGS, ugcDocument.getTags())
            .set(UGC_STATUS, ugcDocument.getStatus())
            .set(UGC_EXTRA_DATA, ugcDocument.getExtraData())
            .set(UGC_GMT_MODIFIED, System.currentTimeMillis());
        return mongoTemplate.updateFirst(query, updateDef, UgcDocument.class);
    }

    public UpdateResult updateStatusByUgcId(String ugcId, String ugcStatus) {
        Query query = new Query(Criteria.where(UGC_ID).is(ugcId));
        Update updateDef = new Update()
            .set(UGC_STATUS, ugcStatus)
            .set(UGC_GMT_MODIFIED, System.currentTimeMillis());
        return mongoTemplate.updateFirst(query, updateDef, UgcDocument.class);
    }

    public UpdateResult updateUgcStatistics(String ugcId, long incrViewCount, long incrLikeCount, long incrCollectCount, long incrCommentaryCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_ID).is(ugcId));
        Update updateDef = new Update();
        if (incrViewCount > 0) {
            updateDef.inc(UGC_VIEW_COUNT, incrViewCount);
        }
        if (incrLikeCount > 0) {
            updateDef.inc(UGC_LIKE_COUNT, incrLikeCount);
        }
        if (incrCollectCount > 0) {
            updateDef.inc(UGC_COLLECT_COUNT, incrCollectCount);
        }
        if (incrCommentaryCount > 0) {
            updateDef.inc(UGC_COMMENTARY_COUNT, incrCommentaryCount);
        }
        updateDef.set(UGC_GMT_MODIFIED, System.currentTimeMillis());
        return mongoTemplate.updateFirst(query, updateDef, UgcDocument.class);
    }

    public UgcDocument queryByUgcId(String ugcId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_ID).is(ugcId));
        return mongoTemplate.findOne(query, UgcDocument.class);
    }

    public List<UgcDocument> queryBatchByUgcId(List<String> ugcIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_ID).in(ugcIds));
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryBatchByAuthorId(String authorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(UGC_AUTHOR_ID).is(authorId));
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryWithTimeCursor(
        String keyword,
        String ugcType,
        String categoryId,
        String ugcStatus,
        String authorId,
        Collection<String> tags,
        Boolean hasSolved,
        long cursor,
        int size
    ) {
        Query query = new Query();
        buildUgcCategoryQueryCondition(query, categoryId);
        buildUgcAuthorQueryCondition(query, authorId);
        buildUgcTagQueryCondition(query, tags);
        buildUgcTypeQueryCondition(query, ugcType);
        buildUgcStatusQueryCondition(query, ugcStatus);
        buildKeywordQueryCondition(query, keyword);
        buildUgcQuestionHasSolvedQueryCondition(query, hasSolved);
        buildTimeCursorQueryCondition(query, cursor, size);
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryWithTimeCursor(
        String keyword,
        String ugcType,
        String categoryId,
        String ugcStatus,
        String authorId,
        Collection<String> tags,
        long cursor,
        int size
    ) {
        Query query = new Query();
        buildUgcCategoryQueryCondition(query, categoryId);
        buildUgcAuthorQueryCondition(query, authorId);
        buildUgcTagQueryCondition(query, tags);
        buildUgcTypeQueryCondition(query, ugcType);
        buildUgcStatusQueryCondition(query, ugcStatus);
        buildKeywordQueryCondition(query, keyword);
        buildTimeCursorQueryCondition(query, cursor, size);
        return mongoTemplate.find(query, UgcDocument.class);
    }

    public List<UgcDocument> queryWithTimeCursor(
        String keyword,
        String ugcType,
        String categoryId,
        String ugcStatus,
        Collection<String> authorIds,
        Collection<String> tags,
        long cursor,
        int size
    ) {
        Query query = new Query();
        buildUgcCategoryQueryCondition(query, categoryId);
        buildUgcAuthorQueryCondition(query, authorIds);
        buildUgcTagQueryCondition(query, tags);
        buildUgcTypeQueryCondition(query, ugcType);
        buildUgcStatusQueryCondition(query, ugcStatus);
        buildKeywordQueryCondition(query, keyword);
        buildTimeCursorQueryCondition(query, cursor, size);
        return mongoTemplate.find(query, UgcDocument.class);
    }

    private void buildUgcStatusQueryCondition(Query query, String ugcStatus) {
        if (UgcStatus.ALL != UgcStatus.of(ugcStatus)) {
            // 查询指定状态的 UGC
            query.addCriteria(Criteria.where(UGC_STATUS).is(ugcStatus));
        } else {
            // 查询除 DRAFT, DELETED 的 UGC
            query.addCriteria(Criteria.where(UGC_STATUS).in(includeUgcStatus));
        }
    }

    private void buildUgcCategoryQueryCondition(Query query, String categoryId) {
        if (StringUtils.isNotBlank(categoryId)) {
            query.addCriteria(Criteria.where(UGC_CATEGORY_ID).is(categoryId));
        }
    }

    private void buildUgcAuthorQueryCondition(Query query, String authorId) {
        if (StringUtils.isNotBlank(authorId)) {
            query.addCriteria(Criteria.where(UGC_AUTHOR_ID).is(authorId));
        }
    }

    private void buildUgcAuthorQueryCondition(Query query, Collection<String> authorIds) {
        if (CollectionUtils.isNotEmpty(authorIds)) {
            query.addCriteria(Criteria.where(UGC_AUTHOR_ID).in(authorIds));
        }
    }

    private void buildUgcTagQueryCondition(Query query, Collection<String> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            query.addCriteria(Criteria.where(UGC_TAGS).in(tags));
        }
    }

    private void buildUgcTypeQueryCondition(Query query, String ugcType) {
        if (UgcType.ALL != UgcType.of(ugcType)) {
            // 查询指定状态的 UGC
            query.addCriteria(Criteria.where(UGC_TYPE).is(ugcType));
        }
    }

    private void buildKeywordQueryCondition(Query query, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return;
        }
        Criteria regexCriteria = new Criteria().orOperator(
            Criteria.where(UGC_TITLE).regex(ofMongoFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION),
            Criteria.where(UGC_SUMMARY).regex(ofMongoFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION),
            Criteria.where(UGC_CONTENT).regex(ofMongoFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION),
            Criteria.where(UGC_TAGS).regex(ofMongoFuzzyQuery(keyword), MONGO_IGNORE_CASE_OPTION)
        );
        query.addCriteria(regexCriteria);
    }

    private void buildUgcQuestionHasSolvedQueryCondition(Query query, Boolean hasSolved) {
        if (Objects.nonNull(hasSolved)) {
            query.addCriteria(Criteria.where(UGC_QUESTION_HAS_SOLVED).is(hasSolved));
        }
    }

    private void buildTimeCursorQueryCondition(Query query, long lastCursor, int size) {
        // 时间倒序查询
        query.addCriteria(Criteria.where(UGC_GMT_MODIFIED).lt(lastCursor));
        query.limit(size);
        query.with(Sort.by(Sort.Direction.DESC, UGC_GMT_MODIFIED, UGC_ID));
    }
}
