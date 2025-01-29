package com.youyi.domain.ugc.core;

import com.youyi.BaseIntegrationTest;
import com.youyi.domain.ugc.cache.UgcStatisticCacheManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
class UgcStatisticCacheManagerTest extends BaseIntegrationTest {
    @Autowired
    UgcStatisticCacheManager ugcStatisticCacheManager;

    @Test
    void incrOrDecrUgcViewCount() {
        String ugcId = "1884572416102768640";
        Assertions.assertDoesNotThrow(() -> ugcStatisticCacheManager.incrOrDecrUgcViewCount(ugcId, false));
    }
}
