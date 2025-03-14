package com.youyi.runner.verification.api;

import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.verification.helper.VerificationHelper;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.domain.verification.request.CaptchaVerifyRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.verification.util.VerificationValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.user.constant.UserConstant.IMAGE_CAPTCHA_KEY;
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

    @RequestMapping(value = "/image/captcha", method = RequestMethod.GET)
    public void notifyImageCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置请求头为输出图片类型
        response.setContentType(MediaType.IMAGE_GIF_VALUE);
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0);
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置验证码类型
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        // 验证码存入session
        request.getSession().setAttribute(IMAGE_CAPTCHA_KEY, specCaptcha.text().toLowerCase());
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }
}
