package com.youyi.domain.ugc.repository.dao;

import com.mongodb.client.result.UpdateResult;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.RepositoryConstant.TOP_COMMENTARY_ID;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_GMT_MODIFIED;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_ID;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_LIKE_COUNT;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_PARENT_ID;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_STATUS;
import static com.youyi.common.constant.UgcConstant.COMMENTARY_UGC_ID;
import static com.youyi.common.constant.UgcConstant.UGC_GMT_MODIFIED;
import static com.youyi.common.constant.UgcConstant.includeCommentaryStatus;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Component
@RequiredArgsConstructor
public class CommentaryDAO {

    private final MongoTemplate mongoTemplate;

    public void save(CommentaryDocument document) {
        mongoTemplate.save(document);
    }

    public List<CommentaryDocument> queryByUgcIdWithTimeCursor(String ugcId, long lastCursor, int size) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_UGC_ID).is(ugcId));
        buildCommentaryStatusQueryCondition(query, CommentaryStatus.ALL.name());
        buildTimeCursorQueryCondition(query, lastCursor, size);
        return mongoTemplate.find(query, CommentaryDocument.class);
    }

    public List<CommentaryDocument> queryRootCommentaryByUgcIdWithTimeCursor(String ugcId, long lastCursor, int size) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_UGC_ID).is(ugcId));
        query.addCriteria(Criteria.where(COMMENTARY_PARENT_ID).is(TOP_COMMENTARY_ID));
        buildCommentaryStatusQueryCondition(query, CommentaryStatus.ALL.name());
        buildTimeCursorQueryCondition(query, lastCursor, size);
        return mongoTemplate.find(query, CommentaryDocument.class);
    }

    public CommentaryDocument queryByCommentaryId(String commentaryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_ID).is(commentaryId));
        return mongoTemplate.findOne(query, CommentaryDocument.class);
    }

    public List<CommentaryDocument> queryByParentId(String parentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_PARENT_ID).is(parentId));
        buildCommentaryStatusQueryCondition(query, CommentaryStatus.ALL.name());
        return mongoTemplate.find(query, CommentaryDocument.class);
    }

    public List<CommentaryDocument> queryWithTimeCursor(long lastCursor, int size) {
        Query query = new Query();
        buildCommentaryStatusQueryCondition(query, CommentaryStatus.ALL.name());
        buildTimeCursorQueryCondition(query, lastCursor, size);
        return mongoTemplate.find(query, CommentaryDocument.class);
    }

    public UpdateResult updateStatusByCommentaryId(String commentaryId, String commentaryStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_ID).is(commentaryId));

        Update updateDef = new Update()
            .set(COMMENTARY_STATUS, commentaryStatus)
            .set(COMMENTARY_GMT_MODIFIED, System.currentTimeMillis());

        return mongoTemplate.updateFirst(query, updateDef, CommentaryDocument.class);
    }

    public UpdateResult batchUpdateStatusByCommentaryId(List<String> commentaryIds, String commentaryStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_ID).in(commentaryIds));

        Update updateDef = new Update()
            .set(COMMENTARY_STATUS, commentaryStatus)
            .set(COMMENTARY_GMT_MODIFIED, System.currentTimeMillis());

        return mongoTemplate.updateMulti(query, updateDef, CommentaryDocument.class);
    }

    public UpdateResult updateCommentaryStatistics(String commentaryId, long incrLikeCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMMENTARY_ID).is(commentaryId));
        Update updateDef = new Update();
        if (incrLikeCount > 0) {
            updateDef.inc(COMMENTARY_LIKE_COUNT, incrLikeCount);
        }
        updateDef.set(UGC_GMT_MODIFIED, System.currentTimeMillis());
        return mongoTemplate.updateFirst(query, updateDef, CommentaryDocument.class);
    }

    private void buildCommentaryStatusQueryCondition(Query query, String commentaryStatus) {
        if (CommentaryStatus.ALL != CommentaryStatus.of(commentaryStatus)) {
            query.addCriteria(Criteria.where(COMMENTARY_STATUS).is(commentaryStatus));
        } else {
            query.addCriteria(Criteria.where(COMMENTARY_STATUS).in(includeCommentaryStatus));
        }
    }

    private void buildTimeCursorQueryCondition(Query query, long lastCursor, int size) {
        // 时间倒序查询
        query.addCriteria(Criteria.where(COMMENTARY_GMT_MODIFIED).lt(lastCursor));
        query.limit(size);
        query.with(Sort.by(Sort.Direction.DESC, COMMENTARY_GMT_MODIFIED, COMMENTARY_ID));
    }
}
