package com.youyi.runner.user.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.user.helper.PermissionHelper;
import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.request.PermissionAddRequest;
import com.youyi.domain.user.request.RolePermissionAuthorizeRequest;
import com.youyi.domain.user.request.RolePermissionRevokeRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.user.util.PermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.constant.PermissionConstant.ADD_PERMISSION;
import static com.youyi.common.constant.PermissionConstant.AUTHORIZE_PERMISSION;
import static com.youyi.common.constant.PermissionConstant.PERMISSION_MANAGER;
import static com.youyi.common.constant.PermissionConstant.REVOKE_PERMISSION;
import static com.youyi.domain.user.assembler.PermissionAssembler.PERMISSION_ASSEMBLER;
import static com.youyi.runner.user.util.PermissionResponseUtil.addSuccess;
import static com.youyi.runner.user.util.PermissionResponseUtil.authorizeSuccess;
import static com.youyi.runner.user.util.PermissionResponseUtil.revokeSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionHelper permissionHelper;

    @SaCheckPermission(value = {PERMISSION_MANAGER, ADD_PERMISSION}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.ADD_PERMISSION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Boolean> addPermission(@RequestBody PermissionAddRequest request) {
        PermissionValidator.checkPermissionAddRequest(request);
        PermissionDO permissionDO = PERMISSION_ASSEMBLER.toPermissionDO(request);
        permissionHelper.addPermission(permissionDO);
        return addSuccess(request);
    }

    @SaCheckPermission(value = {PERMISSION_MANAGER, AUTHORIZE_PERMISSION}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.AUTHORIZE_PERMISSION)
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public Result<Boolean> authorize(@RequestBody RolePermissionAuthorizeRequest request) {
        PermissionValidator.checkRolePermissionAuthorizeRequest(request);
        PermissionDO permissionDO = PERMISSION_ASSEMBLER.toPermissionDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> permissionHelper.authorize(permissionDO),
            CommonOperationUtil::tooManyRequestError,
            request.getRole()
        );
        return authorizeSuccess(request);
    }

    @SaCheckPermission(value = {PERMISSION_MANAGER, REVOKE_PERMISSION}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.REVOKE_PERMISSION)
    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public Result<Boolean> revoke(@RequestBody RolePermissionRevokeRequest request) {
        PermissionValidator.checkRolePermissionRevokeRequest(request);
        PermissionDO permissionDO = PERMISSION_ASSEMBLER.toPermissionDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> permissionHelper.revoke(permissionDO),
            CommonOperationUtil::tooManyRequestError,
            request.getRole()
        );
        return revokeSuccess(request);
    }
}
