package com.youyi.runner.user.util;

import com.google.common.collect.Lists;
import com.youyi.common.type.BizType;
import com.youyi.common.type.user.IdentityType;
import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserSetPwdRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import org.apache.commons.lang3.tuple.Pair;

import static com.youyi.common.util.param.ParamChecker.captchaChecker;
import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.equalsChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.passwordChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class UserValidator {

    public static void checkUserAuthenticateRequest(UserAuthenticateRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(ParamChecker.enumExistChecker(IdentityType.class, "登录类型不合法"), request.getIdentityType())
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA.name().equals(request.getIdentityType()) || IdentityType.EMAIL_PASSWORD.name().equals(request.getIdentityType()),
                emailChecker("邮箱格式不合法"),
                request.getIdentifier()
            )
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA.name().equals(request.getIdentityType()),
                captchaChecker("验证码不合法"),
                request.getCredential()
            )
            .putIf(
                () -> IdentityType.EMAIL_PASSWORD.name().equals(request.getIdentityType()),
                passwordChecker("密码不合法"),
                request.getCredential()
            )
            .validateWithThrow();
    }

    public static void checkUserVerifyCaptchaRequest(UserVerifyCaptchaRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(emailChecker("邮箱格式不合法"), request.getEmail())
            .put(captchaChecker("验证码不合法"), request.getCaptcha())
            .put(enumExistChecker(BizType.class, "业务类型不合法"), request.getBizType())
            .validateWithThrow();
    }

    public static void checkUserSetPwdRequestAndToken(UserSetPwdRequest request, String token) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("token不能为空"), token)
            .put(notBlankChecker("新密码不能为空"), request.getNewPassword())
            .put(notBlankChecker("确认密码不能为空"), request.getConfirmPassword())
            .putBatch(passwordChecker("密码不合法"), Lists.newArrayList(request.getNewPassword(), request.getConfirmPassword()))
            .put(equalsChecker("新密码与确认密码不一致"), Pair.of(request.getNewPassword(), request.getConfirmPassword()))
            .validateWithThrow();
    }
}
