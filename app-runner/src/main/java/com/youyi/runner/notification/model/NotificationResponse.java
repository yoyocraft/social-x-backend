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

    @SerializedName("summary")
    private String summary;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("targetType")
    private String targetType;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("senderName")
    private String senderName;

    @SerializedName("senderAvatar")
    private String senderAvatar;

    @SerializedName("read")
    private Boolean read;

    @SerializedName("gmtCreate")
    private Long gmtCreate;

    @SerializedName("followed")
    private Boolean followed;
}
