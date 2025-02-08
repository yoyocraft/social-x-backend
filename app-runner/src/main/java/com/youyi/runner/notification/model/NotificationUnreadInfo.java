package com.youyi.runner.notification.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationUnreadInfo {

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("unreadCount")
    private Long unreadCount;
}
