package com.youyi.runner.user.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserEditInfoRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import com.youyi.runner.user.model.VerifyCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.user.util.UserConverter.USER_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class UserResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResponseUtil.class);

    public static Result<Boolean> loginSuccess(UserAuthenticateRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("user login, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<UserBasicInfoResponse> getCurrentUserSuccess(UserDO userDO) {
        Result<UserBasicInfoResponse> response = Result.success(USER_CONVERTER.toResponse(userDO));
        LOGGER.info("get current user, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> logoutSuccess() {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("user logout, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<VerifyCaptchaResponse> verifyCaptchaSuccess(UserDO userDO, UserVerifyCaptchaRequest request) {
        Result<VerifyCaptchaResponse> response = Result.success(USER_CONVERTER.toVerifyCaptchaResponse(userDO));
        LOGGER.info("verify captcha, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> setPwdSuccess() {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("set password, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> editUserInfoSuccess(UserEditInfoRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("edit user info, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
