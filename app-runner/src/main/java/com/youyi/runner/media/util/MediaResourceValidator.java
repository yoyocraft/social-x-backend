package com.youyi.runner.media.util;

import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.media.request.ImageUploadRequest;
import com.youyi.domain.media.type.MediaSource;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notNullChecker;
import static com.youyi.infra.conf.core.Conf.getLongConfig;
import static com.youyi.infra.conf.core.ConfigKey.MAX_UPLOAD_FILE_SIZE;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
public class MediaResourceValidator {

    public static void checkImageUploadRequestAndFile(ImageUploadRequest request, MultipartFile file) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(MediaSource.class, "资源来源不合法"), request.getSource())
            .put(notNullChecker("文件不能为空"), file)
            .put(lessThanOrEqualChecker(getLongConfig(MAX_UPLOAD_FILE_SIZE), "文件过大"), Optional.ofNullable(file).orElseThrow().getSize())
            .validateWithThrow();
    }
}
