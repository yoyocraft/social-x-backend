package com.youyi.runner.user.util;

import com.youyi.common.type.BizType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.type.IdentityType;
import com.youyi.domain.user.type.WorkDirectionType;
import com.youyi.infra.conf.core.ConfigKey;
import com.youyi.runner.user.model.UserAuthenticateRequest;
import com.youyi.runner.user.model.UserEditInfoRequest;
import com.youyi.runner.user.model.UserFollowRequest;
import com.youyi.runner.user.model.UserQueryRequest;
import com.youyi.runner.user.model.UserSetPwdRequest;
import com.youyi.runner.user.model.UserVerifyCaptchaRequest;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

import static com.youyi.common.util.param.ParamChecker.captchaChecker;
import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumCodeExistChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.equalsChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.snowflakeIdChecker;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class UserValidator {

    public static void checkUserAuthenticateRequest(UserAuthenticateRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(IdentityType.class, "登录类型不合法"), request.getIdentityType())
            .putIf(
                () -> {
                    IdentityType identityType = IdentityType.of(request.getIdentityType());
                    return identityType == IdentityType.EMAIL_CAPTCHA || identityType == IdentityType.EMAIL_PASSWORD;
                },
                emailChecker(),
                request.getIdentifier()
            )
            .putIf(
                () -> IdentityType.EMAIL_CAPTCHA == IdentityType.of(request.getIdentityType()),
                captchaChecker(),
                request.getCredential()
            )
            .putIf(
                () -> IdentityType.EMAIL_PASSWORD == IdentityType.of(request.getIdentityType()),
                notBlankChecker("密码不能为空"),
                request.getCredential()
            )
            .putBatchIf(
                () -> IdentityType.EMAIL_PASSWORD == IdentityType.of(request.getIdentityType()),
                notBlankChecker("验证码不能为空"),
                Optional.ofNullable(request.getExtra()).orElseGet(Map::of).values()
            )
            .validateWithThrow();
    }

    public static void checkUserVerifyCaptchaRequest(UserVerifyCaptchaRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(emailChecker(), request.getEmail())
            .put(captchaChecker(), request.getCaptcha())
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
            .put(snowflakeIdChecker("用户ID不合法"), request.getUserId())
            .put(notBlankChecker("昵称不能为空"), request.getNickname())
            .put(notBlankChecker("工作开始时间不能为空"), request.getWorkStartTime())
            .put(enumCodeExistChecker(WorkDirectionType.class, "工作方向不合法"), request.getWorkDirection())
            .put(notBlankChecker("简介不能为空"), request.getBio())
            .put(notBlankChecker("工作公司不能为空"), request.getCompany())
            .put(notBlankChecker("工作职位不能为空"), request.getJobTitle())
            .validateWithThrow();
    }

    public static void checkUserFollowRequest(UserFollowRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("关注用户ID不合法"), request.getFollowUserId())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUserQueryRequest(UserQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUserQueryRequestForQueryUserInfo(UserQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("用户ID不合法"), request.getUserId())
            .validateWithThrow();
    }

    public static void checkUserQueryRequestForSuggestedUsers(UserQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }
}
