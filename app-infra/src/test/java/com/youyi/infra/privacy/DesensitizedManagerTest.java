package com.youyi.infra.privacy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
class DesensitizedManagerTest {

    @Test
    void testDesensitizeEmail() {
        String result = DesensitizedManager.desensitizeEmail("youyi@example.com");
        Assertions.assertEquals("y****@example.com", result);
    }

    @Test
    void testDesensitizePhone() {
        String result = DesensitizedManager.desensitizeMobile("12345678901");
        Assertions.assertEquals("123****8901", result);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme