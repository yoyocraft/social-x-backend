package com.youyi.runner.verification.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class VerificationResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationResponseUtil.class);

    public static Result<Boolean> notifyCaptchaSuccess(CaptchaVerifyRequest request) {
        Result<Boolean> response = Result.success(true);
        LOGGER.info("notify captcha, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
