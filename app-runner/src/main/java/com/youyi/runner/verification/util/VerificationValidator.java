package com.youyi.runner.verification.util;

import com.youyi.common.type.BizType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;

import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class VerificationValidator {

    public static void checkCaptchaVerifyRequest(CaptchaVerifyRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(emailChecker(), request.getEmail())
            .put(enumExistChecker(BizType.class, "业务类型不合法"), request.getBizType())
            .validateWithThrow();
    }
}
