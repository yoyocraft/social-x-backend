package com.youyi.domain.ugc.repository.dao;

import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.UgcConstant.UGC_ATTACHMENT_URLS;
import static com.youyi.common.constant.UgcConstant.UGC_AUTHOR_ID;
import static com.youyi.common.constant.UgcConstant.UGC_CONTENT;
import static com.youyi.common.constant.UgcConstant.UGC_COVER;
import static com.youyi.common.constant.UgcConstant.UGC_EXTRA_DATA;
import static com.youyi.common.constant.UgcConstant.UGC_GMT_MODIFIED;
import static com.youyi.common.constant.UgcConstant.UGC_ID;
import static com.youyi.common.constant.UgcConstant.UGC_STATUS;
import static com.youyi.common.constant.UgcConstant.UGC_SUMMARY;
import static com.youyi.common.constant.UgcConstant.UGC_TAGS;
import static com.youyi.common.constant.UgcConstant.UGC_TITLE;
import static com.youyi.common.type.ugc.UgcStatusType.DELETED;
import static com.youyi.common.type.ugc.UgcStatusType.DRAFT;

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

    public Page<UgcDocument> queryByKeywordAndStatusForSelf(String keyword, String ugcStatus, String authorId, int page, int size) {
        Query query = new Query();
        if (UgcStatusType.ALL != UgcStatusType.of(ugcStatus)) {
            // 查询指定状态的 UGC
            query.addCriteria(Criteria.where(UGC_STATUS).is(ugcStatus));
        } else {
            // 查询除 DRAFT, DELETED 的 UGC
            List<String> excludeStatus = Lists.newArrayList(DRAFT.name(), DELETED.name());
            query.addCriteria(Criteria.where(UGC_STATUS).nin(excludeStatus));
        }
        if (StringUtils.isNotBlank(keyword)) {
            Criteria regexCriteria = new Criteria().orOperator(
                Criteria.where(UGC_TITLE).regex(".*" + keyword + ".*", "i"),
                Criteria.where(UGC_SUMMARY).regex(".*" + keyword + ".*", "i")
            );
            query.addCriteria(regexCriteria);
        }
        query.addCriteria(Criteria.where(UGC_AUTHOR_ID).is(authorId));

        // 分页 && 排序
        query.with(PageRequest.of(page, size));
        query.with(Sort.by(Sort.Direction.DESC, UGC_GMT_MODIFIED, UGC_ID));

        // 数据 && 总数
        List<UgcDocument> documents = mongoTemplate.find(query, UgcDocument.class);
        long total = mongoTemplate.count(query, UgcDocument.class);

        return new PageImpl<>(documents, PageRequest.of(page, size), total);
    }

    public List<UgcDocument> queryByStatusWithTimeCursor(String ugcStatus, LocalDateTime lastCursor, int size) {
        Query query = new Query();
        if (UgcStatusType.ALL != UgcStatusType.of(ugcStatus)) {
            query.addCriteria(Criteria.where(UGC_STATUS).is(ugcStatus));
        } else {
            // 查询除 DRAFT, DELETED 的 UGC
            List<String> excludeStatus = Lists.newArrayList(DRAFT.name(), DELETED.name());
            query.addCriteria(Criteria.where(UGC_STATUS).nin(excludeStatus));
        }
        // 分页 && 排序
        query.addCriteria(Criteria.where(UGC_GMT_MODIFIED).lt(lastCursor));
        query.limit(size);
        query.with(Sort.by(Sort.Direction.DESC, UGC_GMT_MODIFIED, UGC_ID));
        return mongoTemplate.find(query, UgcDocument.class);
    }
}
