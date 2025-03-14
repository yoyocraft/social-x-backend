package com.youyi.domain.notification.model;

import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.notification.repository.po.NotificationPO;
import com.youyi.domain.notification.type.NotificationStatus;
import com.youyi.domain.notification.type.NotificationType;
import com.youyi.domain.user.model.UserDO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Getter
@Setter
public class NotificationDO {

    private String notificationId;
    private NotificationType notificationType;
    private NotificationStatus notificationStatus;
    private UserDO sender;
    private UserDO receiver;
    private Long readAt;
    private NotificationExtraData extraData;

    // for query
    private String cursor;
    private Integer size;

    // for unread query
    private Long unreadCount;
    private Boolean queryAll;

    private LocalDateTime gmtCreate;
    private Boolean followed;

    private String content;
    private String title;

    public void create() {
        this.notificationId = IdSeqUtil.genNotificationId();
        this.notificationStatus = NotificationStatus.UNREAD;
    }

    public NotificationPO buildToSaveNotificationPO() {
        NotificationPO notificationPO = new NotificationPO();
        notificationPO.setNotificationId(notificationId);
        notificationPO.setNotificationType(notificationType.name());
        notificationPO.setNotificationStatus(notificationStatus.getCode());
        notificationPO.setSenderId(sender.getUserId());
        notificationPO.setReceiverId(receiver.getUserId());
        notificationPO.setExtraData(GsonUtil.toJson(extraData));
        return notificationPO;
    }

    public void fillWithPO(NotificationPO notificationPO) {
        this.notificationId = notificationPO.getNotificationId();
        this.notificationType = NotificationType.of(notificationPO.getNotificationType());
        this.notificationStatus = NotificationStatus.of(notificationPO.getNotificationStatus());
        this.readAt = notificationPO.getReadAt();
        this.extraData = GsonUtil.fromJson(notificationPO.getExtraData(), NotificationExtraData.class);
        this.gmtCreate = notificationPO.getGmtCreate();
    }
}
