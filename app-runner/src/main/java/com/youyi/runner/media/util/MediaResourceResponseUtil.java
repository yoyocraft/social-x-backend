package com.youyi.runner.media.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.domain.media.request.ImageUploadRequest;
import com.youyi.runner.media.model.ImageUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.media.util.MediaResourceConverter.MEDIA_RESOURCE_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
public class MediaResourceResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaResourceResponseUtil.class);

    public static Result<ImageUploadResponse> uploadImageSuccess(MediaResourceDO mediaResourceDO, ImageUploadRequest request) {
        Result<ImageUploadResponse> response = Result.success(MEDIA_RESOURCE_CONVERTER.toImageUploadResponse(mediaResourceDO));
        LOGGER.info("upload image, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
