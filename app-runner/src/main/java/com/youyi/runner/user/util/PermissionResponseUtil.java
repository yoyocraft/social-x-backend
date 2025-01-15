package com.youyi.runner.user.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.request.PermissionAddRequest;
import com.youyi.domain.user.request.RolePermissionAuthorizeRequest;
import com.youyi.domain.user.request.RolePermissionRevokeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
public class PermissionResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionResponseUtil.class);

    public static Result<Boolean> addSuccess(PermissionAddRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("add permission, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> authorizeSuccess(RolePermissionAuthorizeRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("authorize permission, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> revokeSuccess(RolePermissionRevokeRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("revoke permission, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
