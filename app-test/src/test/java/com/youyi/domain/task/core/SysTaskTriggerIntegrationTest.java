package com.youyi.domain.task.core;

import com.youyi.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/30
 */
class SysTaskTriggerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    SysTaskTrigger sysTaskTrigger;

    @Test
    void testTrigger() {
        Assertions.assertDoesNotThrow(() -> sysTaskTrigger.trigger());
    }
}
