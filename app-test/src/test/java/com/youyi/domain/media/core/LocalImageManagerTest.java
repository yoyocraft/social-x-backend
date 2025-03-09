package com.youyi.domain.media.core;

import com.youyi.BaseIntegrationTest;
import com.youyi.domain.media.model.MediaResourceDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
class LocalImageManagerTest extends BaseIntegrationTest {

    @Autowired
    LocalImageManager localImageManager;

    @Test
    void testGetMediaResourceByUrl() {
        String url = "/media/image/post/2025/03/09/code_bear_17414999358865891577740024121901.jpg";
        MediaResourceDO resourceDO = localImageManager.getMediaResourceByUrl(url);
        Assertions.assertNotNull(resourceDO);
        Assertions.assertNotNull(resourceDO.getMedia());
    }
}
