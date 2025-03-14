package com.youyi.runner.verification.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;
import com.youyi.runner.verification.model.ImageCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.verification.util.VerificationConverter.VERIFICATION_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class VerificationResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(VerificationResponseUtil.class);

    public static Result<Boolean> notifyCaptchaSuccess(CaptchaVerifyRequest request) {
        Result<Boolean> response = Result.success(true);
        logger.info("notify captcha, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<ImageCaptchaResponse> notifyImageCaptchaSuccess(VerificationDO verificationDO) {
        Result<ImageCaptchaResponse> response = Result.success(VERIFICATION_CONVERTER.toResponse(verificationDO));
        logger.info("notify image captcha, response:{}", GsonUtil.toJson(response));
        return response;
    }
}
