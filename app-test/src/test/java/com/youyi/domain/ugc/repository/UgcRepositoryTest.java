package com.youyi.domain.ugc.repository;

import com.google.common.collect.Lists;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.List;
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

    @Autowired
    UgcRepository ugcRepository;

    @Test
    void testSaveUgc() {
        for (int i = 0; i < 100; i++) {
            ugcRepository.saveUgc(buildUgcDocument(i + 1));
        }
    }

    @Test
    void testIncrUgcStatisticCount() {
        String ugcId = "1884571668249976832";
        ugcRepository.incrUgcStatisticCount(ugcId, 0L, 0L, 0L, 0L);
    }

    @Test
    void testQueryInfoWithIdCursorAndExtraData() {
        List<UgcDocument> ugcDocuments = Assertions.assertDoesNotThrow(() -> ugcRepository.queryInfoWithIdCursorAndExtraData(
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

    UgcDocument buildUgcDocument(int base) {
        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setCategoryId("1883785981502304257");
        ugcDocument.setCategoryName("后端");
        ugcDocument.setAttachmentUrls(Lists.newArrayList("/media/avatar/default.jpg", "/media/avatar/default.jpg"));
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.ARTICLE.name());
        ugcDocument.setAuthorId("1883827647466573824");
        ugcDocument.setContent("""
            当然！这里有几个有趣的脑筋急转弯，试试看你能不能猜出来！🤔
                        
            ---
                        
            1. **什么东西越用越多，但永远装不满？** \s
               💡 **答案：** 知识（因为学得越多，知道的就越多） \s
                        
            2. **什么书从来不单独卖？** \s
               💡 **答案：** 说明书（因为总是和商品一起出现） \s
                        
            3. **为什么企鹅的肚子是白色的？** \s
               💡 **答案：** 因为如果肚子是黑的，翻过来就找不到啦！ \s
                        
            4. **什么东西有头无尾，有眼无眉，能看不能动？** \s
               💡 **答案：** 鱼（鱼头、鱼眼，但尾巴不算“尾”，鱼本身能动，但谜面是“能看不能动”） \s
                        
            5. **什么东西下雨时是干的，晴天是湿的？** \s
               💡 **答案：** 影子（晴天太阳晒出影子，下雨时没影子） \s
                        
            6. **什么字所有人都会念错？** \s
               💡 **答案：** “错”字（因为题目问的就是“什么字所有人都会念错”） \s
                        
            7. **什么东西你打它，它哭；你不打它，它也哭？** \s
               💡 **答案：** 洋葱（切洋葱会流泪，放久了烂了也会“哭”） \s
                        
            8. **什么桥不能走？** \s
               💡 **答案：** 郑板桥（人名谐音梗） \s
                        
            ---
                        
            试试考考朋友，看他们能猜中几个！😉 如果有其他需求，比如更难的或特定主题的，可以告诉我～
            """);
        ugcDocument.setCover("/media/avatar/default.jpg");
        ugcDocument.setTitle("【测试】文章" + base);
        ugcDocument.setSummary("【测试】摘要" + base);
        ugcDocument.setTags(Lists.newArrayList("面试技巧", "简历优化", "职场经验"));
        ugcDocument.setViewCount(1000L);
        ugcDocument.setLikeCount(1000L);
        ugcDocument.setCollectCount(100L);
        ugcDocument.setCommentaryCount(1000L);
        ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        ugcDocument.setGmtCreate(System.currentTimeMillis());
        ugcDocument.setGmtModified(System.currentTimeMillis());
        UgcExtraData extraData = new UgcExtraData();
        extraData.setAuditRet("审核通过");
        ugcDocument.setExtraData(extraData);
        return ugcDocument;
    }
}
