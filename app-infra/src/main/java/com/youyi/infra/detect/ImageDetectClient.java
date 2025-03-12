package com.youyi.infra.detect;

import com.youyi.infra.detect.client.ImageDetectAdapter;
import com.youyi.infra.detect.model.ImageDetectRequest;
import com.youyi.infra.detect.model.ImageDetectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
@Component
@RequiredArgsConstructor
public class ImageDetectClient {

    private final ImageDetectAdapter imageDetectAdapter;

    public ImageDetectResponse checkImage(ImageDetectRequest request) {
        try {
            return imageDetectAdapter.checkImage(request);
        } catch (Exception ex) {
            return ImageDetectResponse.DEFAULT_INSTANCE;
        }
    }
}
