package com.youyi.domain.ugc.repository.dao;

import com.google.common.collect.Lists;
import com.youyi.domain.ugc.repository.document.UgcDocument;
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
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.UgcConstant.UGC_AUTHOR_ID;
import static com.youyi.common.constant.UgcConstant.UGC_GMT_MODIFIED;
import static com.youyi.common.constant.UgcConstant.UGC_ID;
import static com.youyi.common.constant.UgcConstant.UGC_STATUS;
import static com.youyi.common.constant.UgcConstant.UGC_SUMMARY;
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

    public Page<UgcDocument> queryByKeywordAndStatusForSelf(String keyword, String ugcStatus, String authorId, int page, int size) {
        Query query = new Query();
        if (StringUtils.isNotBlank(ugcStatus)) {
            query.addCriteria(Criteria.where(UGC_STATUS).is(ugcStatus));
        } else {
            // 查询除 DRAFT, DELETED 的 UGC
            List<String> excludeStatus = Lists.newArrayList(DRAFT.name(), DELETED.name());
            query.addCriteria(Criteria.where(UGC_STATUS).nin(excludeStatus));
        }
        if (StringUtils.isNotBlank(keyword)) {
            query.addCriteria(Criteria.where(UGC_TITLE).regex(".*" + keyword + ".*", "i"))
                .addCriteria(Criteria.where(UGC_SUMMARY).regex(".*" + keyword + ".*", "i"));
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
}
