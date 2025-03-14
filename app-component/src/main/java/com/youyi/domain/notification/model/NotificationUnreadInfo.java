package com.youyi.domain.notification.model;

import com.youyi.domain.notification.type.NotificationType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationUnreadInfo {

    private String notificationType;
    private Long unreadCount;

    public static NotificationUnreadInfo of(NotificationType notificationType, Long unreadCount) {
        NotificationUnreadInfo info = new NotificationUnreadInfo();
        info.notificationType = notificationType.name();
        info.unreadCount = unreadCount;
        return info;
    }
}
