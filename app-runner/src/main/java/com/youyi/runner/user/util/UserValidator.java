package com.youyi.runner.user.util;

import com.google.common.collect.Lists;
import com.youyi.common.type.BizType;
import com.youyi.common.type.user.IdentityType;
import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.param.UserAuthenticateParam;
import com.youyi.domain.user.param.UserSetPwdParam;
import com.youyi.domain.user.param.UserVerifyCaptchaParam;
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
            .put(enumExistChecker(BizType.class, "业务类型不合法"), param.getBizType())
            .validateWithThrow();
    }

    public static void checkUserSetPwdParamAndToken(UserSetPwdParam param, String token) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("token不能为空"), token)
            .put(notBlankChecker("新密码不能为空"), param.getNewPassword())
            .put(notBlankChecker("确认密码不能为空"), param.getConfirmPassword())
            .putBatch(passwordChecker("密码不合法"), Lists.newArrayList(param.getNewPassword(), param.getConfirmPassword()))
            .put(equalsChecker("新密码与确认密码不一致"), Pair.of(param.getNewPassword(), param.getConfirmPassword()))
            .validateWithThrow();
    }
}
