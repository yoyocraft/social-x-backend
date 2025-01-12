package com.youyi.runner.notification.api;

import com.youyi.common.anno.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.OperationType;
import com.youyi.domain.notification.helper.NotificationHelper;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.param.CaptchaNotifyParam;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.notification.util.NotificationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.type.ReturnCode.TOO_MANY_REQUEST;
import static com.youyi.domain.notification.assembler.NotificationAssembler.NOTIFICATION_ASSEMBLER;
import static com.youyi.runner.notification.util.NotificationResponseUtil.notifyCaptchaSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationHelper notificationHelper;

    @RecordOpLog(opType = OperationType.NOTIFY_CAPTCHA, system = true)
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    public Result<Boolean> notifyCaptcha(@RequestBody CaptchaNotifyParam param) {
        NotificationValidator.checkCaptchaNotifyParam(param);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(param);
        LocalLockUtil.runWithLockFailSafe(
            () -> notificationHelper.notifyCaptcha(notificationDO),
            () -> {
                throw AppBizException.of(TOO_MANY_REQUEST);
            },
            param.getEmail(), param.getBizType()
        );
        return notifyCaptchaSuccess(param);
    }
}
