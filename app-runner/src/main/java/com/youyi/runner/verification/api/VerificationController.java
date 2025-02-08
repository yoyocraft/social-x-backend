package com.youyi.runner.verification.api;

import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.verification.helper.VerificationHelper;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.verification.util.VerificationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.verification.assembler.VerificationAssembler.VERIFICATION_ASSEMBLER;
import static com.youyi.runner.verification.util.VerificationResponseUtil.notifyCaptchaSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationHelper verificationHelper;

    @RecordOpLog(opType = OperationType.NOTIFY_CAPTCHA)
    @RequestMapping(value = "/email/captcha", method = RequestMethod.POST)
    public Result<Boolean> notifyEmailCaptcha(@RequestBody CaptchaVerifyRequest request) {
        VerificationValidator.checkCaptchaVerifyRequest(request);
        VerificationDO verificationDO = VERIFICATION_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> verificationHelper.verifyEmailCaptcha(verificationDO),
            CommonOperationUtil::tooManyRequestError,
            request.getEmail(), request.getBizType()
        );
        return notifyCaptchaSuccess(request);
    }
}
