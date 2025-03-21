package com.youyi.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
        logger.info("{}", CommonOperationUtil.date2Timestamp(now));
        logger.info("{}", now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme