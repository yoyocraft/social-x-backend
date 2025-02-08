package com.youyi.domain.notification.repository.mapper;

import com.youyi.domain.notification.model.NotificationUnreadInfo;
import com.youyi.domain.notification.repository.po.NotificationPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Mapper
public interface NotificationMapper {

    int insert(NotificationPO notificationPO);

    List<NotificationPO> querySelfByTypeWithCursor(
        @Param("type") String notificationType,
        @Param("receiverId") String receiverId,
        @Param("cursor") String cursor,
        @Param("size") Integer size
    );

    void updateStatusByNotificationIdWithReadTime(
        @Param("notificationId") String notificationId,
        @Param("receiverId") String receiverId,
        @Param("status") Integer status,
        @Param("oldStatus") Integer oldStatus,
        @Param("readAt") Long readAt
    );

    void updateStatusByReceiverIdWithReadTime(
        @Param("receiverId") String receiverId,
        @Param("status") Integer status,
        @Param("oldStatus") Integer oldStatus,
        @Param("readAt") Long readAt
    );

    void updateStatusByTypeAndReceiverIdWithReadTime(
        @Param("type") String type,
        @Param("receiverId") String receiverId,
        @Param("status") Integer status,
        @Param("oldStatus") Integer oldStatus,
        @Param("readAt") Long readAt
    );

    List<NotificationUnreadInfo> queryUnreadCountGroupByType(@Param("receiverId") String receiverId);

    Long queryUnreadCount(@Param("receiverId") String receiverId);

}
