package com.youyi.runner.user.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.param.UserAuthenticateParam;
import com.youyi.domain.user.param.UserVerifyCaptchaParam;
import com.youyi.runner.user.model.UserVO;
import com.youyi.runner.user.model.VerifyCaptchaVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.user.util.UserConverter.USER_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class UserResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResponseUtil.class);

    public static Result<Boolean> loginSuccess(UserAuthenticateParam param) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("user login, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<UserVO> getCurrentUserSuccess(UserDO userDO) {
        Result<UserVO> response = Result.success(USER_CONVERTER.toVO(userDO));
        LOGGER.info("get current user, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> logoutSuccess() {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("user logout, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<VerifyCaptchaVO> verifyCaptchaSuccess(UserDO userDO, UserVerifyCaptchaParam param) {
        Result<VerifyCaptchaVO> response = Result.success(USER_CONVERTER.toVerifyCaptchaVO(userDO));
        LOGGER.info("verify captcha, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }
}
