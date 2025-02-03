package com.youyi.runner.config.util;

import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.runner.config.model.ConfigInfoResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/31
 */
@Mapper
public interface ConfigConverter {

    ConfigConverter CONFIG_CONVERTER = Mappers.getMapper(ConfigConverter.class);

    @Mappings({
        @Mapping(target = "lastModified", expression = "java(dateToLong(configDO.getGmtModified()))"),
        @Mapping(target = "configType", expression = "java(configDO.getConfigType().name())"),
        @Mapping(target = "deleted", expression = "java(configDO.getDeletedAt() != 0L)")
    })
    ConfigInfoResponse toResponse(ConfigDO configDO);

    default long dateToLong(LocalDateTime date) {
        return date.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

}
