package com.youyi.runner.notification.util;

import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.notification.type.NotificationType;
import com.youyi.infra.conf.core.ConfigKey;
import com.youyi.runner.notification.model.NotificationPublishRequest;
import com.youyi.runner.notification.model.NotificationQueryRequest;
import com.youyi.runner.notification.model.NotificationReadRequest;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.snowflakeIdChecker;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
public class NotificationValidator {

    public static void checkNotificationPublishRequest(NotificationPublishRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(NotificationType.class, "通知类型不合法"), request.getNotificationType())
            .put(notBlankChecker("通知内容不能为空"), request.getContent())
            .put(notBlankChecker("通知标题不能为空"), request.getTitle())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkNotificationQueryRequest(NotificationQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor 不能为空"), request.getCursor())
            .put(enumExistChecker(NotificationType.class, "通知类型不合法"), request.getNotificationType())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkNotificationReadRequestForSingleRead(NotificationReadRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("通知ID不合法"), request.getNotificationId())
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
