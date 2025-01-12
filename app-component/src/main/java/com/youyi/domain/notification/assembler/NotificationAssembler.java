package com.youyi.domain.notification.assembler;

import com.youyi.common.type.BizType;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.param.CaptchaNotifyParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Mapper(
    imports = {
        BizType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationAssembler {
    NotificationAssembler NOTIFICATION_ASSEMBLER = Mappers.getMapper(NotificationAssembler.class);

    @Mappings({
        @Mapping(target = "bizType", expression = "java(BizType.of(param.getBizType()))")
    })
    NotificationDO toDO(CaptchaNotifyParam param);
}
