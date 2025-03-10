package com.youyi.domain.ugc.repository;

import com.github.javafaker.Faker;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
class UgcRepositoryTest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(UgcRepositoryTest.class);

    private static final Faker faker = new Faker(new Random(System.currentTimeMillis()));

    private static final Random random = new Random(System.currentTimeMillis());

    @Autowired
    UgcRepository ugcRepository;

    @Test
    void testSaveUgc() {
        String categoryId = "1883785981502304257";
        String categoryName = "后端";
        for (int i = 0; i < 100; i++) {
            ugcRepository.saveUgc(buildUgcDocument(categoryId, categoryName));
        }
    }

    @Test
    void testIncrUgcStatisticCount() {
        String ugcId = "1884571668249976832";
        ugcRepository.incrUgcStatisticCount(ugcId, 0L, 0L, 0L, 0L);
    }

    @Test
    void testQueryInfoWithIdCursorAndExtraData() {
        List<UgcDocument> ugcDocuments = Assertions.assertDoesNotThrow(() -> ugcRepository.queryByExtraDataWithTimeCursor(
            "",
            "QUESTION",
            "PUBLISHED",
            Boolean.FALSE,
            System.currentTimeMillis(),
            15
        ));

        Assertions.assertNotNull(ugcDocuments);
        logger.info("ugcDocuments: {}", GsonUtil.toJson(ugcDocuments));
    }

    @Test
    void testUpdateDocument() {
        UgcDocument ugcDocument = Assertions.assertDoesNotThrow(() -> ugcRepository.queryByUgcId("1895473020530466816"));
        ugcDocument.setExtraData(new UgcExtraData());
        Assertions.assertDoesNotThrow(() -> ugcRepository.updateUgc(ugcDocument));
    }

    UgcDocument buildUgcDocument(String categoryId, String categoryName) {
        String authorId = "1883827647466573824";
        String title = faker.book().title();
        String summary = faker.lorem().paragraph(20);

        String cover = "/media/cover/default_cover.png";
        String auditRet = "审核通过";
        UgcExtraData extraData = new UgcExtraData();
        extraData.setAuditRet(auditRet);
        extraData.setHasSolved(Boolean.FALSE);
        extraData.setUgcSummary(SymbolConstant.EMPTY);

        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.ARTICLE.name());
        ugcDocument.setAuthorId(authorId);
        ugcDocument.setTitle(title);
        ugcDocument.setContent(faker.lorem().paragraph());
        ugcDocument.setSummary(summary);
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setTags(List.of("tag_1"));
        ugcDocument.setViewCount(getRandomLong(1000, 10000));
        ugcDocument.setLikeCount(getRandomLong(100, 1000));
        ugcDocument.setCollectCount(getRandomLong(100, 1000));
        ugcDocument.setCommentaryCount(getRandomLong(100, 1000));
        ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        ugcDocument.setCover(cover);
        ugcDocument.setGmtCreate(System.currentTimeMillis());
        ugcDocument.setGmtModified(System.currentTimeMillis());
        ugcDocument.setExtraData(extraData);
        return ugcDocument;
    }

    public static Long getRandomLong(long min, long max) {
        return random.nextLong(max - min + 1) + min;
    }
}
