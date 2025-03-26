package com.youyi.infra.cache.key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/26
 */
class UgcCacheKeyRepoTest {

    @Test
    void testOfUgcViewCountKey() {
        String result = UgcCacheKeyRepo.ofUgcViewCountKey("ugcId");
        Assertions.assertEquals("ugc:ugcId:sv", result);
    }

    @Test
    void testOfUgcLikeCountKey() {
        String result = UgcCacheKeyRepo.ofUgcLikeCountKey("ugcId");
        Assertions.assertEquals("ugc:ugcId:sl", result);
    }

    @Test
    void testOfUgcCollectCountKey() {
        String result = UgcCacheKeyRepo.ofUgcCollectCountKey("ugcId");
        Assertions.assertEquals("ugc:ugcId:sc", result);
    }

    @Test
    void testOfUgcCommentaryCountKey() {
        String result = UgcCacheKeyRepo.ofUgcCommentaryCountKey("ugcId");
        Assertions.assertEquals("ugc:ugcId:scom", result);
    }

    @Test
    void testOfCommentaryLikeCountKey() {
        String result = UgcCacheKeyRepo.ofCommentaryLikeCountKey("commentaryId");
        Assertions.assertEquals("cmt:commentaryId:cl", result);
    }

    @Test
    void testOfUgcUserRecommendTagKey() {
        String result = UgcCacheKeyRepo.ofUgcUserRecommendTagKey("userId");
        Assertions.assertEquals("ugc:u:userId:rt", result);
    }

    @Test
    void testOfHotUgcListKey() {
        String result = UgcCacheKeyRepo.ofHotUgcListKey("ugcType");
        Assertions.assertEquals("ugc:hot:ugcType", result);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme