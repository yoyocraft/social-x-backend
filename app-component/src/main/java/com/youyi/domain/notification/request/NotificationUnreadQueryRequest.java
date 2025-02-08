package com.youyi.domain.notification.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationUnreadQueryRequest extends BaseRequest {

    @SerializedName("queryAll")
    private Boolean queryAll;
}
