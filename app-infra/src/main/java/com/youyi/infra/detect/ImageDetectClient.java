package com.youyi.infra.detect;

import com.youyi.common.util.GsonUtil;
import com.youyi.infra.detect.client.ImageDetectAdapter;
import com.youyi.infra.detect.model.ImageDetectRequest;
import com.youyi.infra.detect.model.ImageDetectResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
@Component
@RequiredArgsConstructor
public class ImageDetectClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageDetectClient.class);

    private final ImageDetectAdapter imageDetectAdapter;

    public ImageDetectResponse checkImage(ImageDetectRequest request) {
        try {
            return imageDetectAdapter.checkImage(request);
        } catch (Exception ex) {
            logger.error("check image error, request:{}, error:{}", GsonUtil.toJson(request), ex.getMessage());
            return ImageDetectResponse.DEFAULT_INSTANCE;
        }
    }
}
