package com.youyi.runner.notification.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationUnreadResponse extends BaseResponse {

    @SerializedName("unreadInfoList")
    private List<NotificationUnreadInfo> unreadInfoList;

    public static NotificationUnreadResponse of(List<NotificationUnreadInfo> unreadInfoList) {
        NotificationUnreadResponse response = new NotificationUnreadResponse();
        response.setUnreadInfoList(unreadInfoList);
        return response;
    }
}
