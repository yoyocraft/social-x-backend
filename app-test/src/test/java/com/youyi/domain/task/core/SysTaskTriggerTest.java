package com.youyi.domain.task.core;

import com.youyi.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/30
 */
class SysTaskTriggerTest extends BaseIntegrationTest {

    @Autowired
    SysTaskTrigger sysTaskTrigger;

    @Test
    void testTrigger() {
        sysTaskTrigger.trigger();
    }
}
