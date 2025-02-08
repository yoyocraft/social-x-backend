package com.youyi.runner.notification.util;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.notification.request.NotificationQueryRequest;
import com.youyi.domain.notification.request.NotificationReadRequest;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
public class NotificationValidator {

    public static void checkNotificationQueryRequest(NotificationQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(NotificationType.class, "通知类型不合法"), request.getNotificationType())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkNotificationReadRequestForSingleRead(NotificationReadRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("通知id不能为空"), request.getNotificationId())
            .validateWithThrow();
    }

    public static void checkNotificationReadRequestForTypeRead(NotificationReadRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(NotificationType.class, "通知类型不合法"), request.getNotificationType())
            .validateWithThrow();
    }

    public static void checkNotificationReadRequestForReadAll(NotificationReadRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }
}
