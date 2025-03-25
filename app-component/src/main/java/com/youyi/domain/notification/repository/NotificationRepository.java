package com.youyi.domain.notification.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.notification.model.NotificationUnreadInfo;
import com.youyi.domain.notification.repository.mapper.NotificationMapper;
import com.youyi.domain.notification.repository.po.NotificationPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Repository
@RequiredArgsConstructor
public class NotificationRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRepository.class);

    private final NotificationMapper notificationMapper;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MYSQL;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MYSQL_ERROR;
    }

    public void insert(NotificationPO po) {
        checkNotNull(po);
        int ret = executeWithExceptionHandling(() -> notificationMapper.insert(po));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    public List<NotificationPO> querySelfByTypeWithCursor(String notificationType, String receiverId, String cursor, Integer size) {
        checkState(StringUtils.isNoneBlank(notificationType, receiverId) && size > 0);
        return executeWithExceptionHandling(() -> notificationMapper.querySelfByTypeWithCursor(notificationType, receiverId, cursor, size));
    }

    public void updateStatusByNotificationIdWithReadTime(String notificationId, String receiverId, Integer status, Integer oldStatus, Long readAt) {
        checkState(StringUtils.isNoneBlank(notificationId, receiverId) && readAt > 0);
        executeWithExceptionHandling(() -> notificationMapper.updateStatusByNotificationIdWithReadTime(notificationId, receiverId, status, oldStatus, readAt));
    }

    public void updateStatusByReceiverIdWithReadTime(String receiverId, Integer status, Integer oldStatus, Long readAt) {
        checkState(StringUtils.isNotBlank(receiverId) && readAt > 0);
        executeWithExceptionHandling(() -> notificationMapper.updateStatusByReceiverIdWithReadTime(receiverId, status, oldStatus, readAt));
    }

    public void updateStatusByTypeAndReceiverIdWithReadTime(String type, String receiverId, Integer status, Integer oldStatus, Long readAt) {
        checkState(StringUtils.isNoneBlank(type, receiverId) && readAt > 0);
        executeWithExceptionHandling(() -> notificationMapper.updateStatusByTypeAndReceiverIdWithReadTime(type, receiverId, status, oldStatus, readAt));
    }

    public List<NotificationUnreadInfo> queryUnreadCountGroupByType(String receiverId) {
        checkState(StringUtils.isNotBlank(receiverId));
        return executeWithExceptionHandling(() -> notificationMapper.queryUnreadCountGroupByType(receiverId));
    }

    public Long queryUnreadCount(String receiverId) {
        checkState(StringUtils.isNotBlank(receiverId));
        return executeWithExceptionHandling(() -> notificationMapper.queryUnreadCount(receiverId));
    }
}
