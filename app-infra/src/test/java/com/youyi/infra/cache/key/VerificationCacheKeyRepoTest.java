package com.youyi.infra.cache.key;

import com.youyi.common.type.BizType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
class VerificationCacheKeyRepoTest {

    @Test
    void testOfEmailCaptchaKey() {
        String result = VerificationCacheKeyRepo.ofEmailCaptchaKey("email", BizType.LOGIN);
        Assertions.assertEquals("em:cap:email:LOGIN", result);
    }

    @Test
    void testOfImageCaptchaKey() {
        String ret = VerificationCacheKeyRepo.ofImageCaptchaKey("captchaId");
        Assertions.assertEquals("img:cap:captchaId", ret);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme