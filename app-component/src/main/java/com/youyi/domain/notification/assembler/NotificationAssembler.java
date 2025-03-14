package com.youyi.domain.notification.assembler;

import com.youyi.common.constant.RepositoryConstant;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.request.NotificationPublishRequest;
import com.youyi.domain.notification.request.NotificationQueryRequest;
import com.youyi.domain.notification.request.NotificationReadRequest;
import com.youyi.domain.notification.request.NotificationUnreadQueryRequest;
import com.youyi.domain.notification.type.NotificationType;
import com.youyi.infra.conf.util.CommonConfUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/07
 */
@Mapper(
    imports = {
        NotificationType.class,
        CommonConfUtil.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationAssembler {

    NotificationAssembler NOTIFICATION_ASSEMBLER = Mappers.getMapper(NotificationAssembler.class);

    @Mappings({
        @Mapping(target = "notificationType", expression = "java(NotificationType.of(request.getNotificationType()))"),
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))"),
        @Mapping(target = "cursor", expression = "java(calCursor(request))")
    })
    NotificationDO toDO(NotificationQueryRequest request);

    @Mappings({
        @Mapping(target = "notificationType", expression = "java(NotificationType.of(request.getNotificationType()))")
    })
    NotificationDO toDO(NotificationReadRequest request);

    @Mappings({
        @Mapping(target = "queryAll", source = "queryAll")
    })
    NotificationDO toDO(NotificationUnreadQueryRequest request);

    NotificationDO toDO(NotificationPublishRequest request);

    default String calCursor(NotificationQueryRequest request) {
        if (RepositoryConstant.INIT_QUERY_CURSOR.equals(request.getCursor())) {
            return null;
        }
        return request.getCursor();
    }
}
