package com.youyi.infra.detect.client;

import com.youyi.common.util.GsonUtil;
import com.youyi.infra.detect.model.ImageDetectRequest;
import com.youyi.infra.detect.model.ImageDetectResponse;
import com.youyi.infra.http.HttpUtil;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.youyi.infra.conf.core.Conf.getStringConfig;
import static com.youyi.infra.conf.core.ConfigKey.IMAGE_DETECT_API_URL;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
@Component
public class ImageDetectAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ImageDetectAdapter.class);

    public ImageDetectResponse checkImage(ImageDetectRequest request) {
        CompletableFuture<String> future = HttpUtil.sendPostRequest(
            getStringConfig(IMAGE_DETECT_API_URL),
            request
        );

        try {
            String response = future.join();
            ImageDetectResponse detectResponse = GsonUtil.fromJson(response, ImageDetectResponse.class);
            logger.info("check image, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(detectResponse));
            return detectResponse;
        } catch (Exception ex) {
            logger.error("check image error, request:{}, error:{}", GsonUtil.toJson(request), ex.getMessage());
            throw ex;
        }
    }
}
