package com.youyi.domain.ugc.repository;

import com.github.javafaker.Faker;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.constant.RepositoryConstant;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.ugc.model.CommentaryExtraData;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.type.CommentaryStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/10
 */
class MockCommentaryRunnerIntegrationTest extends BaseIntegrationTest {

    private static final Faker faker = new Faker(new Random(System.currentTimeMillis()));

    private static final Random random = new Random(System.currentTimeMillis());

    private static final List<String> ATTACHMENT_URLS = List.of(
        "/media/attachment/attachment_1.png",
        "/media/attachment/attachment_2.png",
        "/media/attachment/attachment_3.png",
        "/media/attachment/attachment_4.png",
        "/media/attachment/attachment_5.png",
        "/media/attachment/attachment_6.png",
        "/media/attachment/attachment_7.png",
        "/media/attachment/attachment_8.png",
        "/media/attachment/attachment_9.png",
        "/media/attachment/attachment_10.png",
        "/media/attachment/attachment_11.png",
        "/media/attachment/attachment_12.png"
    );

    private static final List<String> AUTHOR_IDS = List.of(
        "1883827647466573824",
        "1883829503093772288"
        // "1896484864405671936"
    );

    private static final List<String> UGC_IDS = List.of(
        // "1899292582322642944"
    );

    private static final List<String> COMMENTARY_IDS = List.of(
        RepositoryConstant.TOP_COMMENTARY_ID
    );

    @Autowired
    CommentaryRepository commentaryRepository;

    @Test
    void testSaveAllCommentary() {
        int turnCount = 1;
        List<CommentaryDocument> commentaryDocumentList = new ArrayList<>();
        for (String ugcId : UGC_IDS) {
            AUTHOR_IDS.forEach(authorId -> {
                COMMENTARY_IDS.forEach(commentaryId -> {
                    for (int i = 0; i < turnCount; i++) {
                        CommentaryDocument comment = buildCommentaryDocument(ugcId, authorId, commentaryId);
                        commentaryDocumentList.add(comment);
                    }
                });
            });
        }
        commentaryRepository.saveAllCommentary(commentaryDocumentList);
    }

    CommentaryDocument buildCommentaryDocument(String ugcId, String authorId, String parentId) {
        CommentaryDocument commentaryDocument = new CommentaryDocument();

        long[] timestamps = generateRandomTimestamps(5);
        long gmtCreate = timestamps[0];
        long gmtModified = timestamps[1];
        commentaryDocument.setCommentaryId(IdSeqUtil.genCommentaryId());
        commentaryDocument.setParentId(parentId);
        commentaryDocument.setUgcId(ugcId);
        commentaryDocument.setCommentatorId(authorId);
        commentaryDocument.setCommentary(genCommentary());
        commentaryDocument.setLikeCount(getRandomLong(10, 100));
        commentaryDocument.setStatus(CommentaryStatus.NORMAL.name());
        commentaryDocument.setAttachmentUrls(generateAttachmentUrls());
        commentaryDocument.setExtraData(genExtraData());
        commentaryDocument.setGmtCreate(gmtCreate);
        commentaryDocument.setGmtModified(gmtModified);

        return commentaryDocument;
    }

    private String genCommentary() {
        return faker.lorem().paragraph(20);
    }

    private List<String> generateAttachmentUrls() {
        List<String> attachmentUrls = new ArrayList<>();
        if (!genBoolean()) {
            return attachmentUrls;
        }
        for (int i = 0; i < 3; i++) {
            attachmentUrls.add(ATTACHMENT_URLS.get(random.nextInt(ATTACHMENT_URLS.size())));
        }
        return attachmentUrls;
    }

    public static Long getRandomLong(long min, long max) {
        return random.nextLong(max - min + 1) + min;
    }

    private long[] generateRandomTimestamps(long rangeInDays) {
        long now = System.currentTimeMillis();
        long startTime = now - rangeInDays * 24 * 60 * 60 * 1000;
        long gmtCreate = random.nextLong(startTime, now);
        long gmtModified = random.nextLong(gmtCreate, now);
        return new long[] {gmtCreate, gmtModified};
    }

    private CommentaryExtraData genExtraData() {
        CommentaryExtraData extraData = new CommentaryExtraData();
        extraData.setAdopted(Boolean.FALSE);
        extraData.setFeatured(genBoolean());
        extraData.setSensitive(Boolean.FALSE);
        return extraData;
    }

    private boolean genBoolean() {
        return random.nextBoolean();
    }
}
