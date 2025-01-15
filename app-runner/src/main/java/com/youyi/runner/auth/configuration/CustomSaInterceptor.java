package com.youyi.runner.auth.configuration;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.youyi.common.type.ReturnCode.PERMISSION_DENIED;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
public class CustomSaInterceptor extends SaInterceptor {

    private static final String ERROR_RESPONSE_CONTENT_TYPE = "application/json;charset=UTF-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            return super.preHandle(request, response, handler);
        } catch (NotPermissionException e) {
            response.setContentType(ERROR_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(GsonUtil.toJson(Result.fail(PERMISSION_DENIED)));
            return false;
        }
    }
}
