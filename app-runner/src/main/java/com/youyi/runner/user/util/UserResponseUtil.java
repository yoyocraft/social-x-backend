package com.youyi.runner.user.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserEditInfoRequest;
import com.youyi.domain.user.request.UserFollowRequest;
import com.youyi.domain.user.request.UserQueryRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import com.youyi.runner.user.model.VerifyCaptchaResponse;
import java.util.List;
import java.util.Optional;
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
        // 脱敏
        request.setCredential(null);
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

    public static Result<Boolean> followUserSuccess(UserFollowRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("follow user, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UserBasicInfoResponse>> queryFollowingUsersSuccess(List<UserDO> userDOList,
        UserQueryRequest request) {
        List<UserBasicInfoResponse> data = userDOList.stream().map(USER_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(userDOList.isEmpty() ? null : userDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);

        Result<PageCursorResult<String, UserBasicInfoResponse>> response = Result.success(PageCursorResult.of(data, cursor));
        LOGGER.info("query following users, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UserBasicInfoResponse>> queryFollowersSuccess(List<UserDO> userDOList, UserQueryRequest request) {
        List<UserBasicInfoResponse> data = userDOList.stream().map(USER_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(userDOList.isEmpty() ? null : userDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        Result<PageCursorResult<String, UserBasicInfoResponse>> response = Result.success(PageCursorResult.of(data, cursor));
        LOGGER.info("query followers, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
