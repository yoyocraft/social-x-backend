package com.youyi.runner.notification.util;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.notification.NotificationStatus;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationExtraData;
import com.youyi.runner.notification.model.NotificationResponse;
import com.youyi.runner.notification.model.NotificationUnreadInfo;
import java.util.Map;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import static com.youyi.common.constant.SystemConstant.DEFAULT_KEY;
import static com.youyi.common.type.conf.ConfigKey.NOTIFICATION_GROUP_CONFIG;
import static com.youyi.infra.conf.core.Conf.getMapConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Mapper(
    imports = {
        NotificationStatus.class
    }
)
public interface NotificationConverter {

    NotificationConverter NOTIFICATION_CONVERTER = Mappers.getMapper(NotificationConverter.class);

    @Mappings({
        @Mapping(target = "notificationId", source = "notificationId"),
        @Mapping(target = "notificationType", expression = "java(notificationDO.getNotificationType().name())"),
        @Mapping(target = "senderId", source = "sender.userId"),
        @Mapping(target = "senderName", source = "sender.nickName"),
        @Mapping(target = "receiverId", source = "receiver.userId"),
        @Mapping(target = "receiverName", source = "receiver.nickName"),
        @Mapping(target = "read", expression = "java(NotificationStatus.READ == notificationDO.getNotificationStatus())"),
        @Mapping(target = "content", expression = "java(getContent(notificationDO))"),
        @Mapping(target = "notificationGroup", expression = "java(getGroup(notificationDO))"),
    })
    NotificationResponse toResponse(NotificationDO notificationDO);

    @Mappings({
        @Mapping(target = "notificationType", expression = "java(notificationDO.getNotificationType().name())"),
        @Mapping(target = "unreadCount", source = "unreadCount"),
    })
    NotificationUnreadInfo toInfo(NotificationDO notificationDO);

    default String getContent(NotificationDO notificationDO) {
        Optional<NotificationExtraData> extraDataOpt = Optional.ofNullable(notificationDO.getExtraData());
        if (extraDataOpt.isEmpty()) {
            return SymbolConstant.EMPTY;
        }
        return extraDataOpt.get().getContent();
    }

    default String getGroup(NotificationDO notificationDO) {
        Map<String, String> groupConfig = getMapConfig(NOTIFICATION_GROUP_CONFIG, String.class, String.class);
        return groupConfig.getOrDefault(notificationDO.getNotificationType().name(), groupConfig.get(DEFAULT_KEY));
    }
}
