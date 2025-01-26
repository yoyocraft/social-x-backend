package com.youyi.runner.media.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.media.helper.MediaResourceHelper;
import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.domain.media.request.ImageUploadRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.media.model.ImageUploadResponse;
import com.youyi.runner.media.util.MediaResourceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.youyi.domain.media.assembler.MediaResourceAssembler.MEDIA_RESOURCE_ASSEMBLER;
import static com.youyi.runner.media.util.MediaResourceResponseUtil.uploadImageSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaResourceController {

    private final MediaResourceHelper mediaResourceHelper;

    @SaCheckLogin
    @RecordOpLog(
        opType = OperationType.UPLOAD_IMAGE,
        desensitize = true,
        fields = {"#request.source"}
    )
    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    public Result<ImageUploadResponse> uploadImage(@RequestPart("file") MultipartFile file, ImageUploadRequest request) {
        MediaResourceValidator.checkImageUploadRequestAndFile(request, file);
        MediaResourceDO mediaResourceDO = MEDIA_RESOURCE_ASSEMBLER.toDO(request, file);
        LocalLockUtil.runWithLockFailSafe(
            () -> mediaResourceHelper.uploadImage(mediaResourceDO),
            CommonOperationUtil::tooManyRequestError,
            request.getSource(), file.getOriginalFilename()
        );
        return uploadImageSuccess(mediaResourceDO, request);
    }
}
