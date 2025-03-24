package com.youyi.domain.user.repository;

import com.google.common.collect.Lists;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.repository.relation.UserNode;
import com.youyi.domain.user.repository.relation.UserRelationship;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
class UserRelationRepositoryIntegrationTest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationRepositoryIntegrationTest.class);

    @Autowired
    UserRelationRepository userRelationRepository;

    @Test
    void testSave() {
        String userId = "444";
        String nickname = "fan3";

        UserNode userNode = userRelationRepository.save(userId, nickname);
        logger.info("userNode: {}", GsonUtil.toJson(userNode));
    }

    @Test
    void saveRelation() {
        UserNode up = new UserNode();
        up.setUserId("111");
        up.setNickname("youyi");

        UserNode up2 = new UserNode();
        up2.setUserId("555");
        up2.setNickname("youyi5");

        UserNode fan = new UserNode();
        fan.setUserId("222");
        fan.setNickname("fan");

        UserRelationship userRelationship = new UserRelationship();
        userRelationship.setSince(System.currentTimeMillis());
        userRelationship.setTarget(up);

        UserRelationship userRelationship2 = new UserRelationship();
        userRelationship2.setSince(System.currentTimeMillis());
        userRelationship2.setTarget(up2);

        fan.setFollowingUsers(Lists.newArrayList(userRelationship, userRelationship2));

        userRelationRepository.saveAll(Lists.newArrayList(fan, up, up2));
    }

    @Test
    void testFindByUserId() {
        UserNode userNode = userRelationRepository.findByUserId("222");
        logger.info("userNode: {}", GsonUtil.toJson(userNode));
    }

    @Test
    void testQueryFollowingUserRelations() {
        List<UserRelationship> relationships = userRelationRepository.queryFollowingUserRelations("222");
        Assertions.assertTrue(CollectionUtils.isNotEmpty(relationships));
    }
}
