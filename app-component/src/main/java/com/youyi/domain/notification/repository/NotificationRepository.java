package com.youyi.domain.notification.repository;

import com.youyi.common.exception.AppSystemException;
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
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRepository.class);

    private final NotificationMapper notificationMapper;

    public void insert(NotificationPO po) {
        try {
            checkNotNull(po);
            int ret = notificationMapper.insert(po);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<NotificationPO> querySelfByTypeWithCursor(String notificationType, String receiverId, String cursor, Integer size) {
        try {
            checkState(StringUtils.isNoneBlank(notificationType, receiverId) && size > 0);
            return notificationMapper.querySelfByTypeWithCursor(notificationType, receiverId, cursor, size);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void updateStatusByNotificationIdWithReadTime(String notificationId, String receiverId, Integer status, Integer oldStatus, Long readAt) {
        try {
            checkState(StringUtils.isNoneBlank(notificationId, receiverId) && readAt > 0);
            notificationMapper.updateStatusByNotificationIdWithReadTime(notificationId, receiverId, status, oldStatus, readAt);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void updateStatusByReceiverIdWithReadTime(String receiverId, Integer status, Integer oldStatus, Long readAt) {
        try {
            checkState(StringUtils.isNotBlank(receiverId) && readAt > 0);
            notificationMapper.updateStatusByReceiverIdWithReadTime(receiverId, status, oldStatus, readAt);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void updateStatusByTypeAndReceiverIdWithReadTime(String type, String receiverId, Integer status, Integer oldStatus, Long readAt) {
        try {
            checkState(StringUtils.isNoneBlank(type, receiverId) && readAt > 0);
            notificationMapper.updateStatusByTypeAndReceiverIdWithReadTime(type, receiverId, status, oldStatus, readAt);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<NotificationUnreadInfo> queryUnreadCountGroupByType(String receiverId) {
        try {
            checkState(StringUtils.isNotBlank(receiverId));
            return notificationMapper.queryUnreadCountGroupByType(receiverId);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public Long queryUnreadCount(String receiverId) {
        try {
            checkState(StringUtils.isNotBlank(receiverId));
            return notificationMapper.queryUnreadCount(receiverId);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
