package com.youyi.runner.user.util;

import com.youyi.domain.user.type.PermissionType;
import com.youyi.domain.user.type.UserRoleType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.user.request.PermissionAddRequest;
import com.youyi.domain.user.request.RolePermissionAuthorizeRequest;
import com.youyi.domain.user.request.RolePermissionRevokeRequest;

import static com.youyi.common.util.param.ParamChecker.collectionNotEmptyChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
public class PermissionValidator {

    public static void checkPermissionAddRequest(PermissionAddRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putBatch(enumExistChecker(PermissionType.class, "权限类型不合法"), request.getPermissions())
            .validateWithThrow();
    }

    public static void checkRolePermissionAuthorizeRequest(RolePermissionAuthorizeRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(UserRoleType.class, "角色类型不合法"), request.getRole())
            .put(collectionNotEmptyChecker("授权列表不能为空"), request.getGrantedPermissions())
            .putBatch(enumExistChecker(PermissionType.class, "权限类型不合法"), request.getGrantedPermissions())
            .validateWithThrow();
    }

    public static void checkRolePermissionRevokeRequest(RolePermissionRevokeRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(UserRoleType.class, "角色类型不合法"), request.getRole())
            .put(collectionNotEmptyChecker("撤销权限列表不能为空"), request.getRevokePermissions())
            .putBatch(enumExistChecker(PermissionType.class, "权限类型不合法"), request.getRevokePermissions())
            .validateWithThrow();
    }
}
