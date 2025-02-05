package com.youyi.runner.verification.util;

import com.youyi.common.type.BizType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;

import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class VerificationValidator {

    public static void checkCaptchaVerifyRequest(CaptchaVerifyRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("email不能为空"), request.getEmail())
            .put(emailChecker("email格式不合法"), request.getEmail())
            .put(enumExistChecker(BizType.class, "业务类型不合法"), request.getBizType())
            .validateWithThrow();
    }
}
