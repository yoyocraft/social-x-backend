package com.youyi.infra.detect;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.constant.ErrorCodeConstant;
import com.youyi.common.util.MediaUtil;
import com.youyi.infra.detect.model.ImageDetectRequest;
import com.youyi.infra.detect.model.ImageDetectResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
class ImageDetectClientIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ImageDetectClient imageDetectClient;

    @Test
    void testCheckImage() {
        ImageDetectRequest request = new ImageDetectRequest();
        request.setImages(List.of(new ImageDetectRequest.ImageInfo(
            "test_key",
            MediaUtil.encodeImageToBase64("/Users/youyi/Documents/hhu/graduation-design/image/post/2025/03/09/code_bear_17414999358865891577740024121901.jpg")
        )));
        ImageDetectResponse response = imageDetectClient.checkImage(request);
        Assertions.assertEquals(ErrorCodeConstant.SUCCESS_CODE, response.getCode());
    }
}
