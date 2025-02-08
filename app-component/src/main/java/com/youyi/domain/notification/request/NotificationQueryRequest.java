package com.youyi.domain.notification.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/06
 */
@Getter
@Setter
public class NotificationQueryRequest extends BasePageRequest {

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("cursor")
    private String cursor;
}
