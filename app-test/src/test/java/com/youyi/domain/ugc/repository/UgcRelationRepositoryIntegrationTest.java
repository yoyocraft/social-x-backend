package com.youyi.domain.ugc.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.repository.relation.UgcInteractInfo;
import com.youyi.domain.ugc.repository.relation.UgcNode;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
class UgcRelationRepositoryIntegrationTest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(UgcRelationRepositoryIntegrationTest.class);

    @Autowired
    UgcRelationshipRepository ugcRelationshipRepository;

    @Test
    void testSave() {
        String ugcId = "1884419546954997760";
        UgcNode ugcNode = ugcRelationshipRepository.save(ugcId);
        logger.info("save ugcNode:{}", GsonUtil.toJson(ugcNode));
    }

    @Test
    void testAddLikeRelationship() {
        String ugcId = "1884419546954997760";
        String userId = "1883829503093772288";
        ugcRelationshipRepository.addLikeRelationship(ugcId, userId);
    }

    @Test
    void testAddCollectRelationship() {
        String ugcId = "1884419546954997760";
        String userId = "1883829503093772288";
        ugcRelationshipRepository.addCollectRelationship(ugcId, userId);
    }

    @Test
    void testDeleteLikeRelationship() {
        String ugcId = "1884419546954997760";
        String userId = "1883829503093772288";
        ugcRelationshipRepository.deleteLikeRelationship(ugcId, userId);
    }

    @Test
    void testQueryCollectedUgcIdsWithCursor() {
        String userId = "1883827647466573824";
        List<UgcInteractInfo> ret = Assertions.assertDoesNotThrow(() -> ugcRelationshipRepository.queryCollectedUgcIdsWithCursor(userId, System.currentTimeMillis(), 10));
        logger.info("queryCollectedUgcIdsWithCursor:{}", GsonUtil.toJson(ret));
    }

}
