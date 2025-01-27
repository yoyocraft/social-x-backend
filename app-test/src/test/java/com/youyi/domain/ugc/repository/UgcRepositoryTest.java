package com.youyi.domain.ugc.repository;

import com.google.common.collect.Lists;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
class UgcRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UgcRepository ugcRepository;

    @Test
    void testSaveUgc() {
        ugcRepository.saveUgc(buildUgcDocument());
    }

    UgcDocument buildUgcDocument() {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.ARTICLE.name());
        ugcDocument.setAuthorId("1L");
        ugcDocument.setTitle("test_title");
        ugcDocument.setContent("test_content");
        ugcDocument.setSummary("test_summary");
        ugcDocument.setTags(Lists.newArrayList("test_tag1", "test_tag2"));
        ugcDocument.setViewCount(1000L);
        ugcDocument.setLikeCount(1000L);
        ugcDocument.setCommentCount(100L);
        ugcDocument.setStatus(UgcStatusType.PUBLISHED.name());
        ugcDocument.setGmtCreate(LocalDateTime.now());
        ugcDocument.setGmtModified(LocalDateTime.now());
        ugcDocument.setExtraData("{}");
        return ugcDocument;
    }
}
