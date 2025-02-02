package com.youyi.domain.conf.assembler;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.conf.ConfigType;
import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.domain.conf.request.ConfigCreateRequest;
import com.youyi.domain.conf.request.ConfigDeleteRequest;
import com.youyi.domain.conf.request.ConfigQueryRequest;
import com.youyi.domain.conf.request.ConfigUpdateRequest;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * 1. Request -> DO
 * 2. DO -> PO
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Mapper(
    imports = {
        ConfigType.class,
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigAssembler {

    ConfigAssembler CONFIG_ASSEMBLER = Mappers.getMapper(ConfigAssembler.class);

    @Mappings({
        @Mapping(target = "configType", expression = "java(ConfigType.of(request.getConfigType()))")
    })
    ConfigDO toDO(ConfigCreateRequest request);

    @Mappings({
        @Mapping(source = "key", target = "configKey"),
        @Mapping(target = "size", expression = "java(calSize(request))")
    })
    ConfigDO toDO(ConfigQueryRequest request);

    @Mappings({
        @Mapping(source = "newConfigValue", target = "configValue"),
        @Mapping(source = "currVersion", target = "version")
    })
    ConfigDO toDO(ConfigUpdateRequest request);

    ConfigDO toDO(ConfigDeleteRequest request);

    default int calSize(ConfigQueryRequest request) {
        if (Objects.isNull(request.getSize()) || request.getSize() <= 0) {
            return getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE);
        }
        return Math.min(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), request.getSize());
    }
}
