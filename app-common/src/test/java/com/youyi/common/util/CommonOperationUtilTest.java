package com.youyi.common.util;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/21
 */
class CommonOperationUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonOperationUtilTest.class);

    @Test
    void testDate2Timestamp() {
        LocalDateTime now = LocalDateTime.now();
        long timestamp = Assertions.assertDoesNotThrow(() -> CommonOperationUtil.date2Timestamp(now));
        logger.info("{}", timestamp);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme