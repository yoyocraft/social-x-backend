package com.youyi.runner.notification.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationResponse extends BaseResponse {

    @SerializedName("notificationId")
    private String notificationId;

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("content")
    private String content;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("senderName")
    private String senderName;

    @SerializedName("receiverId")
    private String receiverId;

    @SerializedName("receiverName")
    private String receiverName;

    @SerializedName("read")
    private Boolean read;

    @SerializedName("notificationGroup")
    private String notificationGroup;
}
