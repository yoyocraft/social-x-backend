package com.youyi.runner.user.util;

import com.youyi.common.type.BizType;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.user.IdentityType;
import com.youyi.common.type.user.WorkDirectionType;
import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserEditInfoRequest;
import com.youyi.domain.user.request.UserFollowRequest;
import com.youyi.domain.user.request.UserQueryRequest;
import com.youyi.domain.user.request.UserSetPwdRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import org.apache.commons.lang3.tuple.Pair;

import static com.youyi.common.util.param.ParamChecker.captchaChecker;
import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumCodeExistChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.equalsChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

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
            .put(equalsChecker("新密码与确认密码不一致"), Pair.of(request.getNewPassword(), request.getConfirmPassword()))
            .validateWithThrow();
    }

    public static void checkUserEditInfoRequest(UserEditInfoRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("用户ID不能为空"), request.getUserId())
            .put(notBlankChecker("昵称不能为空"), request.getNickName())
            .put(notBlankChecker("工作开始时间不能为空"), request.getWorkStartTime())
            .put(enumCodeExistChecker(WorkDirectionType.class, "工作方向不合法"), request.getWorkDirection())
            .put(notBlankChecker("简介不能为空"), request.getBio())
            .put(notBlankChecker("工作公司不能为空"), request.getCompany())
            .put(notBlankChecker("工作职位不能为空"), request.getJobTitle())
            .validateWithThrow();
    }

    public static void checkUserFollowRequest(UserFollowRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("关注用户ID不能为空"), request.getFollowingUserId())
            .put(notBlankChecker("用户ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUserQueryRequest(UserQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .put(notBlankChecker("用户ID不能为空"), request.getUserId())
            .validateWithThrow();
    }
}
