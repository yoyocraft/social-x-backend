package com.youyi.runner.user.util;

import com.youyi.common.type.IdentityType;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.param.UserAuthenticateParam;
import com.youyi.domain.user.param.UserVerifyCaptchaParam;

import static com.youyi.common.util.param.ParamChecker.captchaChecker;
import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.passwordChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class UserValidator {

    public static void checkUserAuthenticateParam(UserAuthenticateParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(ParamChecker.enumExistChecker(IdentityType.class, "登录类型不合法"), param.getIdentityType())
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA.name().equals(param.getIdentityType()) || IdentityType.EMAIL_PASSWORD.name().equals(param.getIdentityType()),
                emailChecker("邮箱格式不合法"),
                param.getIdentifier()
            )
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA.name().equals(param.getIdentityType()),
                captchaChecker("验证码不合法"),
                param.getCredential()
            )
            .putIf(
                () -> IdentityType.EMAIL_PASSWORD.name().equals(param.getIdentityType()),
                passwordChecker("密码不合法"),
                param.getCredential()
            )
            .validateWithThrow();
    }

    public static void checkUserVerifyCaptchaParam(UserVerifyCaptchaParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(emailChecker("邮箱格式不合法"), param.getEmail())
            .put(captchaChecker("验证码不合法"), param.getCaptcha())
            .put(enumExistChecker(NotificationType.class, "业务类型不合法"), param.getBizType())
            .validateWithThrow();
    }
}
