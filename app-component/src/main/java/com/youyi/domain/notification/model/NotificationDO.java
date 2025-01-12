package com.youyi.domain.notification.model;

import com.youyi.common.type.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Getter
@Setter
public class NotificationDO {

    private String email;
    private String captcha;
    private NotificationType notificationType;
}
