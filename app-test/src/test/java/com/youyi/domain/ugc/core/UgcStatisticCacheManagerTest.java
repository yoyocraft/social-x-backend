package com.youyi.domain.ugc.core;

import com.youyi.BaseIntegrationTest;
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
        String ugcId = "1895394388223074304";
        Assertions.assertDoesNotThrow(() -> ugcStatisticCacheManager.incrOrDecrUgcViewCount(ugcId));
    }
}
