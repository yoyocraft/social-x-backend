package com.youyi.runner.notification.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.notification.param.CaptchaNotifyParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class NotificationResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResponseUtil.class);

    public static Result<Boolean> notifyCaptchaSuccess(CaptchaNotifyParam param) {
        Result<Boolean> response = Result.success(true);
        LOGGER.info("notify captcha, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }
}
