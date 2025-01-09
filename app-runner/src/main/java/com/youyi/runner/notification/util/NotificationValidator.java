package com.youyi.runner.notification.util;

import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.notification.param.CaptchaNotifyParam;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class NotificationValidator {

    public static void checkCaptchaNotifyParam(CaptchaNotifyParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(ParamChecker.notBlankChecker("email不能为空"), param.getEmail())
            .put(ParamChecker.emailChecker("email格式不合法"), param.getEmail())
            .validateWithThrow();
    }
}
