package com.youyi.infra.cache.repo;

import com.youyi.common.type.BizType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
class VerificationCacheRepoTest {

    @Test
    void testOfEmailCaptchaKey() {
        String result = VerificationCacheRepo.ofEmailCaptchaKey("test@example.com", BizType.LOGIN);
        Assertions.assertEquals("em:cap:test@example.com:LOGIN", result);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme