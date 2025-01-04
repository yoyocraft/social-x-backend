package com.youyi.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/04
 */
class IvGeneratorTest {

    @Test
    void testGenIv() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> IvGenerator.generateIv(null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> IvGenerator.generateIv(""));

        String baseStr = "18266667777";
        String ret1 = IvGenerator.generateIv(baseStr);
        String ret2 = IvGenerator.generateIv(baseStr);
        Assertions.assertEquals(ret1, ret2);
    }

    @Test
    void testGenIvWithDifferentParameters() {
        String baseStr1 = "18266667777";
        String baseStr2 = "18366667777";
        String baseStr3 = "18466667777";

        String ret1 = IvGenerator.generateIv(baseStr1);
        String ret2 = IvGenerator.generateIv(baseStr2);
        String ret3 = IvGenerator.generateIv(baseStr3);

        Assertions.assertNotEquals(ret1, ret2);
        Assertions.assertNotEquals(ret1, ret3);
        Assertions.assertNotEquals(ret2, ret3);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme