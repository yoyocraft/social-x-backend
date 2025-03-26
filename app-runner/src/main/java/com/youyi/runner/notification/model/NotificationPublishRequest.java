package com.youyi.runner.notification.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/02
 */
@Getter
@Setter
public class NotificationPublishRequest extends BaseRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("notificationType")
    private String notificationType;
}
