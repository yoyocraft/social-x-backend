package com.youyi.runner.notification.util;

import com.youyi.common.type.BizType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.notification.param.CaptchaNotifyParam;

import static com.youyi.common.util.param.ParamChecker.emailChecker;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
public class NotificationValidator {

    public static void checkCaptchaNotifyParam(CaptchaNotifyParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("email不能为空"), param.getEmail())
            .put(emailChecker("email格式不合法"), param.getEmail())
            .put(enumExistChecker(BizType.class, "业务类型不合法"), param.getBizType())
            .validateWithThrow();
    }
}
