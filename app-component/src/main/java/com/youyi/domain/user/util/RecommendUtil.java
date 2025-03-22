package com.youyi.domain.user.util;

import com.youyi.domain.user.model.UserDO;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/22
 */
public class RecommendUtil {

    private static final int RECOMMEND_USER_MAX_COUNT = 10;

    public static List<UserDO> recommendUsers(UserDO currentUser, List<UserDO> users) {
        // 获取当前用户的兴趣标签
        Collection<String> userInterestTags = currentUser.getPersonalizedTags();

        return users.stream()
            .peek(user -> {
                if (CollectionUtils.isNotEmpty(user.getPersonalizedTags())) {
                    double similarity = calculateCosineSimilarity(
                        userInterestTags,
                        user.getPersonalizedTags()
                    );
                    user.setSimilarity(similarity);
                } else {
                    user.setSimilarity(0.0);
                }
            })
            .sorted(Comparator.comparingDouble(UserDO::getSimilarity).reversed())
            .limit(RECOMMEND_USER_MAX_COUNT)
            .toList();
    }

    private static double calculateCosineSimilarity(Collection<String> userInterestTags, Collection<String> matchUserInterestTags) {
        Set<String> intersection = new HashSet<>(userInterestTags);
        intersection.retainAll(matchUserInterestTags);
        double dotProduct = intersection.size();
        double userNorm = Math.sqrt(userInterestTags.size());
        double articleNorm = Math.sqrt(matchUserInterestTags.size());

        return dotProduct / (userNorm * articleNorm);
    }
}
