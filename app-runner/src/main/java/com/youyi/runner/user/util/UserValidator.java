package com.youyi.runner.user.util;

import com.youyi.common.type.IdentityType;
import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.param.UserAuthenticateParam;

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
                ParamChecker.emailChecker("邮箱格式不合法"),
                param.getIdentifier()
            )
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA.name().equals(param.getIdentityType()),
                ParamChecker.captchaChecker("验证码不合法"),
                param.getCredential()
            )
            .putIf(
                () -> IdentityType.EMAIL_PASSWORD.name().equals(param.getIdentityType()),
                ParamChecker.passwordChecker("密码不合法"),
                param.getCredential()
            )
            .validateWithThrow();
    }
}
