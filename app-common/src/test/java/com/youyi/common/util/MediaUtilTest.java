package com.youyi.common.util;

import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/13
 */
class MediaUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(MediaUtilTest.class);

    URL resourceUrl = null;

    @BeforeEach
    void setUp() {
        resourceUrl = getClass().getClassLoader().getResource("test.png");
        Assertions.assertNotNull(resourceUrl);
    }

    @Test
    void testEncodeImageToBase64FromPath() {
        String base64 = MediaUtil.encodeImageToBase64(resourceUrl.getFile());
        Assertions.assertNotNull(base64);
    }

    @Test
    void testGetFileExtension() {
        File resource = new File(resourceUrl.getFile());
        String result = MediaUtil.getFileExtension(resource);
        Assertions.assertEquals("png", result);
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme