package com.youyi.domain.notification.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/05
 */
@Getter
@Setter
public class NotificationPO extends BasePO {

    private String notificationId;

    private String notificationType;

    private Integer notificationStatus;

    private String receiverId;

    private String senderId;

    private Long readAt;
}
