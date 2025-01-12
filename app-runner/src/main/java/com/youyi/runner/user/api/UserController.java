package com.youyi.runner.user.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.anno.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.OperationType;
import com.youyi.domain.user.helper.UserHelper;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.param.UserAuthenticateParam;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.user.model.UserVO;
import com.youyi.runner.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.type.ReturnCode.TOO_MANY_REQUEST;
import static com.youyi.domain.user.assembler.UserAssembler.USER_ASSEMBLER;
import static com.youyi.runner.user.util.UserResponseUtil.getCurrentUserSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.loginSuccess;
import static com.youyi.runner.user.util.UserResponseUtil.logoutSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserHelper userHelper;

    @RecordOpLog(opType = OperationType.USER_LOGIN, system = true)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Boolean> login(@RequestBody UserAuthenticateParam param) {
        UserValidator.checkUserAuthenticateParam(param);
        UserDO userDO = USER_ASSEMBLER.toDO(param);
        LocalLockUtil.runWithLockFailSafe(
            () -> userHelper.login(userDO),
            () -> {
                throw AppBizException.of(TOO_MANY_REQUEST);
            },
            param.getIdentityType(), param.getIdentifier()
        );
        return loginSuccess(param);
    }

    @RequestMapping(value = "/curr", method = RequestMethod.GET)
    public Result<UserVO> getCurrentUser() {
        UserDO userDO = userHelper.getCurrentUser();
        return getCurrentUserSuccess(userDO);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.USER_LOGOUT)
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result<Boolean> logout() {
        userHelper.logout();
        return logoutSuccess();
    }

}
