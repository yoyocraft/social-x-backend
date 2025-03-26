package com.youyi.infra.cache.key;

import com.youyi.common.type.BizType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/26
 */
class UserCacheKeyRepoTest {

    @Test
    void testOfUserVerifyTokenKey() {
        String result = UserCacheKeyRepo.ofUserVerifyTokenKey("email", BizType.LOGIN);
        Assertions.assertEquals("u:vrf:cap:email:LOGIN", result);
    }

    @Test
    void testOfUserFollowIdsKey() {
        String result = UserCacheKeyRepo.ofUserFollowIdsKey("userId");
        Assertions.assertEquals("u:fl:userId", result);
    }

    @Test
    void testOfHotAuthorListKey() {
        String result = UserCacheKeyRepo.ofHotAuthorListKey();
        Assertions.assertEquals("u:hot", result);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme