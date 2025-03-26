package com.youyi.runner.notification.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.notification.hepler.NotificationHelper;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.notification.model.NotificationPublishRequest;
import com.youyi.runner.notification.model.NotificationQueryRequest;
import com.youyi.runner.notification.model.NotificationReadRequest;
import com.youyi.runner.notification.model.NotificationResponse;
import com.youyi.runner.notification.model.NotificationUnreadQueryRequest;
import com.youyi.runner.notification.model.NotificationUnreadResponse;
import com.youyi.runner.notification.util.NotificationValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.constant.PermissionConstant.NOTIFICATION_MANAGER;
import static com.youyi.runner.notification.assembler.NotificationAssembler.NOTIFICATION_ASSEMBLER;
import static com.youyi.runner.notification.util.NotificationResponseUtil.publishNotificationSuccess;
import static com.youyi.runner.notification.util.NotificationResponseUtil.queryNotificationSuccess;
import static com.youyi.runner.notification.util.NotificationResponseUtil.queryUnreadCountSuccess;
import static com.youyi.runner.notification.util.NotificationResponseUtil.readAllNotificationByTypeSuccess;
import static com.youyi.runner.notification.util.NotificationResponseUtil.readSingleNotificationSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationHelper notificationHelper;

    @SaCheckPermission(value = {NOTIFICATION_MANAGER})
    @RecordOpLog(opType = OperationType.PUBLISH_NOTIFICATION)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Result<Boolean> publishNotification(@RequestBody NotificationPublishRequest request) {
        NotificationValidator.checkNotificationPublishRequest(request);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> notificationHelper.publish(notificationDO),
            CommonOperationUtil::tooManyRequestError,
            request.getNotificationType(), request.getReqId()
        );
        return publishNotificationSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<PageCursorResult<String, NotificationResponse>> listNotification(NotificationQueryRequest request) {
        NotificationValidator.checkNotificationQueryRequest(request);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        List<NotificationDO> notificationDOList = notificationHelper.queryByTypeWithCursor(notificationDO);
        return queryNotificationSuccess(notificationDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public Result<Boolean> readSingleNotification(@RequestBody NotificationReadRequest request) {
        NotificationValidator.checkNotificationReadRequestForSingleRead(request);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> notificationHelper.readSingleNotification(notificationDO),
            CommonOperationUtil::tooManyRequestError,
            request.getNotificationId()
        );
        return readSingleNotificationSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/read/type", method = RequestMethod.POST)
    public Result<Boolean> readNotificationByType(@RequestBody NotificationReadRequest request) {
        NotificationValidator.checkNotificationReadRequestForTypeRead(request);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> notificationHelper.readAllNotificationByType(notificationDO),
            CommonOperationUtil::tooManyRequestError,
            request.getNotificationId()
        );
        return readAllNotificationByTypeSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/read/all", method = RequestMethod.POST)
    public Result<Boolean> readAllNotification(@RequestBody NotificationReadRequest request) {
        NotificationValidator.checkNotificationReadRequestForReadAll(request);
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> notificationHelper.readAll(notificationDO),
            CommonOperationUtil::tooManyRequestError,
            request.getReqId()
        );
        return readAllNotificationByTypeSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/unread/count", method = RequestMethod.GET)
    public Result<NotificationUnreadResponse> queryUnreadCount(NotificationUnreadQueryRequest request) {
        NotificationDO notificationDO = NOTIFICATION_ASSEMBLER.toDO(request);
        List<NotificationDO> notificationDOList = notificationHelper.queryUnreadCount(notificationDO);
        return queryUnreadCountSuccess(notificationDOList, request);
    }

}
