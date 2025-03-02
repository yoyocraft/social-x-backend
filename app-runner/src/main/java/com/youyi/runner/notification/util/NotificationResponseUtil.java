package com.youyi.runner.notification.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.request.NotificationPublishRequest;
import com.youyi.domain.notification.request.NotificationQueryRequest;
import com.youyi.domain.notification.request.NotificationReadRequest;
import com.youyi.domain.notification.request.NotificationUnreadQueryRequest;
import com.youyi.runner.notification.model.NotificationResponse;
import com.youyi.runner.notification.model.NotificationUnreadInfo;
import com.youyi.runner.notification.model.NotificationUnreadResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.infra.conf.util.CommonConfUtil.checkHasMore;
import static com.youyi.runner.notification.util.NotificationConverter.NOTIFICATION_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
public class NotificationResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(NotificationResponseUtil.class);

    public static Result<PageCursorResult<String, NotificationResponse>> queryNotificationSuccess(List<NotificationDO> notificationDOList,
        NotificationQueryRequest request) {
        String cursor = Optional.ofNullable(notificationDOList.isEmpty() ? null : notificationDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        List<NotificationResponse> notificationResponseList = notificationDOList.stream().map(NOTIFICATION_CONVERTER::toResponse).toList();
        Result<PageCursorResult<String, NotificationResponse>> response = Result.success(PageCursorResult.of(notificationResponseList, cursor, checkHasMore(notificationResponseList)));
        logger.info("query notification, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> readSingleNotificationSuccess(NotificationReadRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("read single notification, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> readAllNotificationByTypeSuccess(NotificationReadRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("read single notification, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> publishNotificationSuccess(NotificationPublishRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("publish notification, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<NotificationUnreadResponse> queryUnreadCountSuccess(List<NotificationDO> notificationDOList,
        NotificationUnreadQueryRequest request) {
        List<NotificationUnreadInfo> unreadInfos = notificationDOList.stream().map(NOTIFICATION_CONVERTER::toInfo).collect(Collectors.toList());
        Result<NotificationUnreadResponse> response = Result.success(NotificationUnreadResponse.of(unreadInfos));
        logger.info("query unread count group by type, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
