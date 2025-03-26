package com.youyi.runner.notification.assembler;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationExtraData;
import com.youyi.domain.notification.type.NotificationStatus;
import com.youyi.runner.notification.model.NotificationResponse;
import com.youyi.runner.notification.model.NotificationUnreadInfo;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Mapper(
    imports = {
        NotificationStatus.class,
        CommonOperationUtil.class
    }
)
public interface NotificationConverter {

    NotificationConverter NOTIFICATION_CONVERTER = Mappers.getMapper(NotificationConverter.class);

    @Mappings({
        @Mapping(target = "notificationId", source = "notificationId"),
        @Mapping(target = "notificationType", expression = "java(notificationDO.getNotificationType().name())"),
        @Mapping(target = "senderId", source = "sender.userId"),
        @Mapping(target = "senderName", source = "sender.nickname"),
        @Mapping(target = "senderAvatar", source = "sender.avatar"),
        @Mapping(target = "read", expression = "java(NotificationStatus.READ == notificationDO.getNotificationStatus())"),
        @Mapping(target = "content", expression = "java(getContent(notificationDO))"),
        @Mapping(target = "summary", expression = "java(getSummary(notificationDO))"),
        @Mapping(target = "targetId", expression = "java(getTargetId(notificationDO))"),
        @Mapping(target = "targetType", expression = "java(getTargetType(notificationDO))"),
        @Mapping(target = "gmtCreate", expression = "java(CommonOperationUtil.date2Timestamp(notificationDO.getGmtCreate()))"),
        @Mapping(target = "followed", source = "followed")
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

    default String getSummary(NotificationDO notificationDO) {
        Optional<NotificationExtraData> extraDataOpt = Optional.ofNullable(notificationDO.getExtraData());
        if (extraDataOpt.isEmpty()) {
            return SymbolConstant.EMPTY;
        }
        return extraDataOpt.get().getSummary();
    }

    default String getTargetId(NotificationDO notificationDO) {
        Optional<NotificationExtraData> extraDataOpt = Optional.ofNullable(notificationDO.getExtraData());
        if (extraDataOpt.isEmpty()) {
            return SymbolConstant.EMPTY;
        }
        return extraDataOpt.get().getTargetId();
    }

    default String getTargetType(NotificationDO notificationDO) {
        Optional<NotificationExtraData> extraDataOpt = Optional.ofNullable(notificationDO.getExtraData());
        if (extraDataOpt.isEmpty()) {
            return SymbolConstant.EMPTY;
        }
        return extraDataOpt.get().getTargetType();
    }
}
