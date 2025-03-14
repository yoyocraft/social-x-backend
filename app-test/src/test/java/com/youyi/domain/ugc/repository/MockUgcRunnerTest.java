package com.youyi.domain.ugc.repository;

import com.github.javafaker.Faker;
import com.youyi.BaseIntegrationTest;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/10
 */
class MockUgcRunnerTest extends BaseIntegrationTest {

    private static final Faker faker = new Faker(new Random(System.currentTimeMillis()));

    private static final Random random = new Random(System.currentTimeMillis());

    private static final Map<String, String> ARTICLE_CATEGORY_MAP = Map.of(
        "1883785981502304256", "求职",
        "1883785981502304257", "后端",
        "1883785981502304258", "前端",
        "1883785981502304259", "客户端",
        "1883785981502304260", "人工智能",
        "1883785981502304261", "大数据",
        "1883785981502304262", "云计算",
        "1883785981502304263", "代码人生",
        "1883785981502304264", "阅读",
        "1883785981502304265", "开发工具"
    );
    private static final Map<String, String> QUESTION_CATEGORY_MAP = Map.of(
        "1895433964073398272", "学习指南",
        "1895433964073398273", "求职指南",
        "1895433964073398274", "Offer选择",
        "1895433964073398275", "职场指南",
        "1895433964073398276", "Bug解决",
        "1895433964073398277", "技术知识",
        "1895433964073398278", "求资源",
        "1895433964073398279", "其他"
    );

    private static final Map<String, String> POST_CATEGORY_MAP = Map.of(
        "1895083560793944064", "闲聊",
        "1895083560793944065", "学习打卡",
        "1895083560793944066", "学习指南",
        "1895083560793944067", "项目",
        "1895083560793944068", "求职",
        "1895083560793944069", "面试",
        "1895083560793944070", "职场",
        "1895083560793944071", "资源",
        "1895083560793944072", "学习总结",
        "1895083560793944073", "知识碎片"
    );

    private static final Map<String, List<String>> ARTICLE_CATEGORY_TAGS = Map.of(
        "求职", List.of("面试技巧", "简历优化", "职场经验", "面试题", "求职心态"),
        "后端", List.of("Java", "Spring", "数据库", "微服务", "API设计", "Docker", "Redis"),
        "前端", List.of("JavaScript", "React", "Vue", "前端架构", "TypeScript", "CSS", "Web性能优化"),
        "客户端", List.of("Android", "iOS", "Flutter", "React Native", "移动性能优化", "App架构"),
        "人工智能", List.of("机器学习", "深度学习", "自然语言处理", "计算机视觉", "AI算法", "TensorFlow", "PyTorch"),
        "大数据", List.of("Hadoop", "Spark", "Kafka", "数据仓库", "数据挖掘", "数据分析"),
        "云计算", List.of("AWS", "Azure", "GCP", "Kubernetes", "容器化", "云架构"),
        "代码人生", List.of("编程思维", "时间管理", "开发者心态", "职业规划", "学习方法"),
        "阅读", List.of("编程书籍", "技术阅读", "经典书籍", "开发者必读"),
        "开发工具", List.of("Git", "VS Code", "IDE", "Postman", "DevOps")
    );

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
        "1883829503093772288",
        "1896484864405671936"
    );

    @Autowired
    UgcRepository ugcRepository;

    @Test
    void testSaveAllUgc() {
        int turnCount = 150;
        List<UgcDocument> ugcDocuments = new ArrayList<>();
        for (String authorId : AUTHOR_IDS) {
            ARTICLE_CATEGORY_MAP.forEach((key, value) -> {
                for (int i = 0; i < turnCount; i++) {
                    ugcDocuments.add(buildArticle(authorId, key, value));
                }
            });
            QUESTION_CATEGORY_MAP.forEach((key, value) -> {
                for (int i = 0; i < turnCount; i++) {
                    ugcDocuments.add(buildQuestion(authorId, key, value));
                }
            });
            POST_CATEGORY_MAP.forEach((key, value) -> {
                for (int i = 0; i < turnCount; i++) {
                    ugcDocuments.add(buildPost(authorId, key, value));
                }
            });
        }
        ugcRepository.saveAllUgc(ugcDocuments);
    }

    UgcDocument buildArticle(String authorId, String categoryId, String categoryName) {
        String title = faker.book().title();
        String cover = "/media/cover/default_cover.png";

        long[] timestamps = generateRandomTimestamps(30);
        long gmtCreate = timestamps[0];
        long gmtModified = timestamps[1];

        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.ARTICLE.name());
        ugcDocument.setAuthorId(authorId);
        ugcDocument.setTitle(title);
        ugcDocument.setContent(generateArticleContent(title));
        ugcDocument.setSummary(faker.lorem().paragraph(20));
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setTags(getRandomTags(categoryName));
        ugcDocument.setViewCount(getRandomLong(1000, 10000));
        ugcDocument.setLikeCount(getRandomLong(100, 1000));
        ugcDocument.setCollectCount(getRandomLong(100, 1000));
        ugcDocument.setCommentaryCount(getRandomLong(100, 1000));
        ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        ugcDocument.setCover(cover);
        ugcDocument.setGmtCreate(gmtCreate);
        ugcDocument.setGmtModified(gmtModified);
        ugcDocument.setExtraData(generateExtraData());
        return ugcDocument;
    }

    UgcDocument buildQuestion(String authorId, String categoryId, String categoryName) {
        long[] timestamps = generateRandomTimestamps(30);
        long gmtCreate = timestamps[0];
        long gmtModified = timestamps[1];

        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.QUESTION.name());
        ugcDocument.setAuthorId(authorId);
        ugcDocument.setTitle(faker.book().title());
        ugcDocument.setContent(generateQuestionContent());
        ugcDocument.setSummary(faker.lorem().paragraph(20));
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setViewCount(getRandomLong(1000, 10000));
        ugcDocument.setLikeCount(getRandomLong(100, 1000));
        ugcDocument.setCollectCount(getRandomLong(100, 1000));
        ugcDocument.setCommentaryCount(getRandomLong(100, 1000));
        ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        ugcDocument.setGmtCreate(gmtCreate);
        ugcDocument.setGmtModified(gmtModified);
        ugcDocument.setExtraData(generateExtraData());
        return ugcDocument;
    }

    UgcDocument buildPost(String authorId, String categoryId, String categoryName) {
        long[] timestamps = generateRandomTimestamps(15);
        long gmtCreate = timestamps[0];
        long gmtModified = timestamps[1];

        UgcDocument ugcDocument = new UgcDocument();
        ugcDocument.setUgcId(IdSeqUtil.genUgcId());
        ugcDocument.setType(UgcType.POST.name());
        ugcDocument.setAuthorId(authorId);
        ugcDocument.setContent(generatePostContent());
        ugcDocument.setCategoryId(categoryId);
        ugcDocument.setCategoryName(categoryName);
        ugcDocument.setViewCount(getRandomLong(1000, 10000));
        ugcDocument.setLikeCount(getRandomLong(100, 1000));
        ugcDocument.setCollectCount(getRandomLong(100, 1000));
        ugcDocument.setCommentaryCount(getRandomLong(100, 1000));
        ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        ugcDocument.setAttachmentUrls(generateAttachmentUrls());
        ugcDocument.setGmtCreate(gmtCreate);
        ugcDocument.setGmtModified(gmtModified);
        ugcDocument.setExtraData(generateExtraData());
        return ugcDocument;
    }

    private String generatePostContent() {
        return faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(20) + "\n\n" +
            faker.lorem().paragraph(10);
    }

    private List<String> generateAttachmentUrls() {
        List<String> attachmentUrls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            attachmentUrls.add(ATTACHMENT_URLS.get(random.nextInt(ATTACHMENT_URLS.size())));
        }
        return attachmentUrls;
    }

    private String generateQuestionContent() {
        return "## 问题描述\n" +
            faker.lorem().paragraph(15) + "\n\n" +
            faker.lorem().paragraph(20) + "\n\n" +
            "## 背景信息\n" +
            faker.lorem().paragraph(10) + "\n\n" +
            faker.lorem().paragraph(15) + "\n\n" +
            faker.lorem().paragraph(20) + "\n\n" +
            "## 具体需求\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(20) + "\n\n" +
            "## 已尝试方法\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(20) + "\n\n" +
            "## 相关资料\n" +
            faker.lorem().paragraph(15) + "\n\n" +
            faker.lorem().paragraph(10) + "\n\n";
    }

    private String generateArticleContent(String title) {
        return "# " + title + "\n\n" +
            "## 引言\n" +
            faker.lorem().paragraph() + "\n\n" +
            "## 技术背景\n" +
            faker.lorem().paragraph(10) + "\n\n" +
            "## 实现细节\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            faker.lorem().paragraph(30) + "\n\n" +
            "## 示例代码\n" +
            "```java\n" +
            faker.lorem().sentence(10) + "\n```" + "\n\n" +
            "## 总结\n" +
            faker.lorem().paragraph(10) + "\n\n";
    }

    public static List<String> getRandomTags(String category) {
        List<String> tags = new ArrayList<>(ARTICLE_CATEGORY_TAGS.getOrDefault(category, Collections.emptyList()));
        if (tags.size() <= 3) {
            return tags;
        }
        Collections.shuffle(tags);
        return tags.subList(0, 3);
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

    private UgcExtraData generateExtraData() {
        UgcExtraData extraData = new UgcExtraData();
        extraData.setAuditRet("审核通过");
        extraData.setHasSolved(Boolean.FALSE);
        extraData.setUgcSummary(SymbolConstant.EMPTY);
        return extraData;
    }
}
