package com.youyi.domain.ugc.repository;

import com.google.common.collect.Lists;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.RandomGenUtil;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
class UgcRepositoryTest extends BaseIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcRepositoryTest.class);

    @Autowired
    UgcRepository ugcRepository;

    @Test
    void testSaveUgc() {
        ugcRepository.saveUgc(buildUgcDocument());
    }

    @Test
    void testQueryByKeywordAndStatusForSelf() {
        Page<UgcDocument> documents = ugcRepository.queryByKeywordAndStatusForSelf("1", "", "19840225123473844738347675097046", 0, 10);
        LOGGER.info("query result:{}", GsonUtil.toJson(documents));
    }

    UgcDocument buildUgcDocument() {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(RandomGenUtil.genUgcId());
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
