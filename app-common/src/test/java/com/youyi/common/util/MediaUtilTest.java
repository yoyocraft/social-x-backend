package com.youyi.common.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
class MediaUtilTest {

    @Test
    void testEncodeImageToBase64FromPath() {
        String result = MediaUtil.encodeImageToBase64FromPath("/Users/youyi/Documents/hhu/graduation-design/image/post/2025/03/09/code_bear_17414999358865891577740024121901.jpg");
        Assertions.assertTrue(StringUtils.isNotBlank(result));
    }

}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme