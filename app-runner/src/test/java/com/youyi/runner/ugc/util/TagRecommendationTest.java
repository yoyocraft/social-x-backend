package com.youyi.runner.ugc.util;

import com.google.gson.reflect.TypeToken;
import com.youyi.common.util.GsonUtil;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/09
 */
class TagRecommendationTest {

    private static final Logger logger = LoggerFactory.getLogger(TagRecommendationTest.class);

    private static final List<String> allTags = Arrays.asList(
        "面试技巧", "简历优化", "职场经验", "面试题", "求职心态", "Java", "Spring", "数据库", "微服务", "API设计",
        "Docker", "Redis", "JavaScript", "React", "Vue", "前端架构", "TypeScript", "CSS", "Web性能优化",
        "Android", "iOS", "Flutter", "React Native", "移动性能优化", "App架构", "机器学习", "深度学习",
        "自然语言处理", "计算机视觉", "AI算法", "TensorFlow", "PyTorch", "Hadoop", "Spark", "Kafka", "数据仓库",
        "数据挖掘", "数据分析", "AWS", "Azure", "GCP", "Kubernetes", "容器化", "云架构", "编程思维", "时间管理",
        "开发者心态", "职业规划", "学习方法", "编程书籍", "技术阅读", "经典书籍", "开发者必读", "Git",
        "VS Code", "IDE", "Postman", "DevOps"
    );

    private static final String tagRelationShip = """
        {
          "Java": ["Spring", "微服务", "数据库", "JVM", "Kafka", "Docker"],
          "Spring": ["Java", "微服务", "Kafka", "数据库", "Docker"],
          "微服务": ["Java", "Spring", "Kafka", "Docker", "AWS", "Kubernetes"],
          "数据库": ["Java", "Spring", "MySQL", "PostgreSQL", "Docker", "Kafka"],
          "Docker": ["Java", "Spring", "微服务", "Kubernetes", "Redis", "Kafka", "云架构"],
          "Kafka": ["Java", "微服务", "Spring", "数据库", "Docker", "AWS", "Kubernetes"],
          "JavaScript": ["React", "Vue", "前端架构", "TypeScript", "Node.js"],
          "React": ["JavaScript", "前端架构", "Vue", "TypeScript", "移动性能优化", "React Native"],
          "Vue": ["JavaScript", "React", "前端架构", "TypeScript"],
          "前端架构": ["JavaScript", "React", "Vue", "TypeScript", "Node.js"],
          "TypeScript": ["JavaScript", "React", "Vue", "前端架构", "Node.js"],
          "CSS": ["Web性能优化", "前端架构", "React", "Vue"],
          "Web性能优化": ["前端架构", "React", "Vue", "CSS", "JavaScript"],
          "Android": ["iOS", "Flutter", "React Native", "App架构"],
          "iOS": ["Android", "Flutter", "React Native", "App架构"],
          "Flutter": ["Android", "iOS", "React Native", "App架构"],
          "React Native": ["Android", "iOS", "Flutter", "App架构"],
          "机器学习": ["深度学习", "自然语言处理", "AI算法", "Python", "TensorFlow", "PyTorch"],
          "深度学习": ["机器学习", "自然语言处理", "AI算法", "TensorFlow", "PyTorch", "计算机视觉"],
          "自然语言处理": ["机器学习", "深度学习", "AI算法", "Python", "TensorFlow", "PyTorch"],
          "AI算法": ["机器学习", "深度学习", "自然语言处理", "TensorFlow", "PyTorch"],
          "TensorFlow": ["机器学习", "深度学习", "AI算法", "Python"],
          "PyTorch": ["机器学习", "深度学习", "AI算法", "Python", "TensorFlow"],
          "Hadoop": ["大数据", "Spark", "Kafka", "AWS", "云架构"],
          "Spark": ["大数据", "Hadoop", "Kafka", "AWS", "云架构"],
          "大数据": ["Hadoop", "Spark", "Kafka", "云架构", "AWS"],
          "数据仓库": ["大数据", "Hadoop", "Spark", "AWS", "Kafka", "云架构"],
          "数据挖掘": ["机器学习", "数据分析", "数据分析", "深度学习"],
          "数据分析": ["数据挖掘", "机器学习", "大数据", "深度学习"],
          "AWS": ["大数据", "云计算", "Hadoop", "Spark", "Kubernetes", "容器化"],
          "Azure": ["云计算", "AWS", "GCP", "Kubernetes", "DevOps"],
          "GCP": ["云计算", "AWS", "Azure", "Kubernetes", "DevOps"],
          "Kubernetes": ["Docker", "微服务", "AWS", "云架构", "容器化"],
          "容器化": ["Docker", "Kubernetes", "微服务", "云架构"],
          "云架构": ["Kubernetes", "Docker", "微服务", "AWS", "Azure", "GCP"],
          "编程思维": ["时间管理", "学习方法", "开发者心态"],
          "时间管理": ["编程思维", "学习方法", "开发者心态"],
          "开发者心态": ["编程思维", "时间管理", "职业规划"],
          "职业规划": ["开发者心态", "编程思维", "学习方法"],
          "学习方法": ["编程思维", "时间管理", "开发者心态"],
          "编程书籍": ["技术阅读", "经典书籍", "开发者必读"],
          "技术阅读": ["编程书籍", "经典书籍", "开发者必读"],
          "经典书籍": ["编程书籍", "技术阅读", "开发者必读"],
          "Git": ["VS Code", "IDE", "DevOps"],
          "VS Code": ["Git", "IDE", "Postman"],
          "IDE": ["Git", "VS Code", "Postman"],
          "Postman": ["Git", "VS Code", "IDE"],
          "DevOps": ["Git", "VS Code", "Docker", "Kubernetes"]
        }
        """;

    private static final Map<String, Set<String>> tagRelations;

    static {
        tagRelations = GsonUtil.fromJson(tagRelationShip, new TypeToken<>() {
        });
    }

    @Test
    void testRecommend() {
        // 用户兴趣标签
        Set<String> userInterestTags = Set.of("Java", "Spring", "数据库", "微服务");

        // 获取 Top 10 推荐标签
        List<String> recommendedTags = getTop10RecommendedTags(userInterestTags);

        logger.info("推荐的标签：{}", GsonUtil.toJson(recommendedTags));

        // 基于推荐标签从文章库筛选文章（模拟）
        List<Map<String, Set<String>>> articles = getArticleData();
        List<Map<String, Set<String>>> recommendedArticles = getArticlesByTags(recommendedTags, articles);

        logger.info("推荐的文章：{}", GsonUtil.toJson(recommendedArticles));
    }

    private List<Map<String, Set<String>>> getArticleData() {
        List<Map<String, Set<String>>> articles = new ArrayList<>();
        articles.add(createArticle("文章1", new HashSet<>(Arrays.asList("Java", "Spring", "微服务"))));
        articles.add(createArticle("文章2", new HashSet<>(Arrays.asList("React", "前端架构", "TypeScript"))));
        articles.add(createArticle("文章3", new HashSet<>(Arrays.asList("机器学习", "深度学习", "AI算法"))));
        articles.add(createArticle("文章4", new HashSet<>(Arrays.asList("Docker", "Kubernetes", "云架构"))));
        return articles;
    }

    private Map<String, Set<String>> createArticle(String title, Set<String> tags) {
        Map<String, Set<String>> article = new HashMap<>();
        article.put("title", Set.of(title));
        article.put("tags", tags);
        return article;
    }

    private List<String> getTop10RecommendedTags(Set<String> userInterestTags) {
        Map<String, Double> tagScores = new HashMap<>();

        // 扩展用户兴趣标签
        Set<String> extendedUserTags = new HashSet<>(userInterestTags);
        for (String tag : userInterestTags) {
            if (tagRelations.containsKey(tag)) {
                // 添加相关标签
                extendedUserTags.addAll(tagRelations.get(tag));
            }
        }

        // 创建一个最小堆（优先队列），容量为 10
        Queue<Map.Entry<String, Double>> topTags = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue)
        );

        // 计算每个标签与用户兴趣标签的余弦相似度
        for (String tag : allTags) {
            double similarity = calculateCosineSimilarity(extendedUserTags, Collections.singleton(tag));
            if (similarity > 0) {
                // 向堆中插入元素
                topTags.offer(new AbstractMap.SimpleEntry<>(tag, similarity));
                // 保持堆大小为10
                if (topTags.size() > 10) {
                    topTags.poll();  // 移除堆中最小的元素
                }
            }
        }

        // 提取 Top 10 标签
        List<String> top10Tags = new ArrayList<>();
        while (!topTags.isEmpty()) {
            top10Tags.add(topTags.poll().getKey());
        }

        // 由于我们是从小到大堆顶移除的，所以需要反转列表顺序
        Collections.reverse(top10Tags);

        return top10Tags;
    }

    // 计算余弦相似度
    private double calculateCosineSimilarity(Set<String> userInterestTags, Set<String> articleTags) {
        Set<String> intersection = new HashSet<>(userInterestTags);
        intersection.retainAll(articleTags);
        double dotProduct = intersection.size();
        double userNorm = Math.sqrt(userInterestTags.size());
        double articleNorm = Math.sqrt(articleTags.size());

        return dotProduct / (userNorm * articleNorm);
    }

    // 根据推荐标签筛选相关的文章
    private List<Map<String, Set<String>>> getArticlesByTags(List<String> recommendedTags, List<Map<String, Set<String>>> articles) {
        List<Map<String, Set<String>>> recommendedArticles = new ArrayList<>();

        for (Map<String, Set<String>> article : articles) {
            Set<String> articleTags = article.get("tags");
            for (String recommendedTag : recommendedTags) {
                if (articleTags.contains(recommendedTag)) {
                    recommendedArticles.add(article);
                    break;
                }
            }
        }

        return recommendedArticles;
    }
}