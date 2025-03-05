package com.youyi.runner.user.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.user.helper.UserHelper;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserEditInfoRequest;
import com.youyi.domain.user.request.UserFollowRequest;
import com.youyi.domain.user.request.UserQueryRequest;
import com.youyi.domain.user.request.UserSetPwdRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import com.youyi.runner.user.model.VerifyCaptchaResponse;
import com.youyi.runner.user.util.UserValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.user.assembler.UserAssembler.USER_ASSEMBLER;
import static com.youyi.runner.user.util.UserResponseUtil.editUserInfoSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.followUserSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.getCurrentUserSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.getUserInfoSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.querySelfFollowersSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.querySelfFollowingUsersSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.loginSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.logoutSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.setPwdSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.verifyCaptchaSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserHelper userHelper;

    @RecordOpLog(
        opType = OperationType.USER_LOGIN,
        desensitize = true,
        fields = {"#request.identityType", "#request.identifier"}
    )
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Boolean> login(@RequestBody UserAuthenticateRequest request) {
        UserValidator.checkUserAuthenticateRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.login(userDO),
            CommonOperationUtil::tooManyRequestError,
            request.getIdentityType(), request.getIdentifier()
        );
        return loginSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/curr", method = RequestMethod.GET)
    public Result<UserBasicInfoResponse> getCurrentUser() {
        UserDO userDO = userHelper.getCurrentUser();
        return getCurrentUserSuccess(userDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result<UserBasicInfoResponse> getUserInfo(UserQueryRequest request) {
        UserValidator.checkUserQueryRequestForQueryUserInfo(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        UserDO userInfo = userHelper.getUserInfo(userDO);
        return getUserInfoSuccess(userInfo);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.USER_LOGOUT, preRecord = true)
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result<Boolean> logout() {
        userHelper.logout();
        return logoutSuccess();
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.VERIFY_CAPTCHA)
    @RequestMapping(value = "/auth/captcha/verify", method = RequestMethod.POST)
    public Result<VerifyCaptchaResponse> verifyCaptcha(@RequestBody UserVerifyCaptchaRequest request) {
        UserValidator.checkUserVerifyCaptchaRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.verifyCaptcha(userDO),
            CommonOperationUtil::tooManyRequestError,
            request.getBizType(), request.getEmail()
        );
        return verifyCaptchaSuccess(userDO, request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.USER_SET_PASSWORD, desensitize = true, preRecord = true)
    @RequestMapping(value = "/auth/set_pwd", method = RequestMethod.POST)
    public Result<Boolean> setPwd(@RequestBody UserSetPwdRequest request, @RequestHeader("Authorization") String token) {
        UserValidator.checkUserSetPwdRequestAndToken(request, token);
        UserDO userDO = USER_ASSEMBLER.toDO(request, token);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.setPwd(userDO),
            CommonOperationUtil::tooManyRequestError,
            token
        );
        return setPwdSuccess();
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.USER_EDIT_INFO)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Boolean> editUserInfo(@RequestBody UserEditInfoRequest request) {
        UserValidator.checkUserEditInfoRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.editUserInfo(userDO),
            CommonOperationUtil::tooManyRequestError,
            request.getUserId()
        );
        return editUserInfoSuccess(request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.USER_FOLLOW_USER)
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public Result<Boolean> followUser(@RequestBody UserFollowRequest request) {
        UserValidator.checkUserFollowRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.followUser(userDO),
            CommonOperationUtil::tooManyRequestError,
            request.getFollowUserId(), request.getReqId()
        );
        return followUserSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/me/following", method = RequestMethod.GET)
    public Result<PageCursorResult<Long, UserBasicInfoResponse>> querySelfFollowingUsers(UserQueryRequest request) {
        UserValidator.checkUserQueryRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        List<UserDO> followingUsers = userHelper.querySelfFollowingUsers(userDO);
        return querySelfFollowingUsersSuccess(followingUsers, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/me/follower", method = RequestMethod.GET)
    public Result<PageCursorResult<Long, UserBasicInfoResponse>> querySelfFollowers(UserQueryRequest request) {
        UserValidator.checkUserQueryRequest(request);
        UserDO userDO = USER_ASSEMBLER.toDO(request);
        List<UserDO> followingUsers = userHelper.queryFollowers(userDO);
        return querySelfFollowersSuccess(followingUsers, request);
    }
}
