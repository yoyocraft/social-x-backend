package com.youyi.domain.ugc.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/19
 */
public class RecommendUtil {

    private static final int RECOMMEND_TAG_MAX_COUNT = 10;

    /**
     * 计算 Top10 推荐给用户的标签
     *
     * @param allTags          所有的标签
     * @param userInterestTags 用户感兴趣的标签
     * @param tagRelations     标签联系，用于计算余弦相似度
     * @return Top10 推荐的标签
     */
    public static List<String> getTop10RecommendedTags(Collection<String> allTags, Collection<String> userInterestTags, Map<String, Set<String>> tagRelations) {
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
            if (similarity <= 0) {
                continue;
            }
            // 向堆中插入元素
            topTags.offer(new AbstractMap.SimpleEntry<>(tag, similarity));
            // 保持堆大小为10
            if (topTags.size() > RECOMMEND_TAG_MAX_COUNT) {
                topTags.poll();
            }
        }

        // 提取 Top 10 标签
        List<String> top10Tags = new ArrayList<>();
        while (!topTags.isEmpty()) {
            top10Tags.add(topTags.poll().getKey());
        }

        Collections.reverse(top10Tags);

        return top10Tags;
    }

    /**
     * 计算余弦相似度
     */
    private static double calculateCosineSimilarity(Set<String> userInterestTags, Set<String> articleTags) {
        Set<String> intersection = new HashSet<>(userInterestTags);
        intersection.retainAll(articleTags);
        double dotProduct = intersection.size();
        double userNorm = Math.sqrt(userInterestTags.size());
        double articleNorm = Math.sqrt(articleTags.size());

        return dotProduct / (userNorm * articleNorm);
    }
}
