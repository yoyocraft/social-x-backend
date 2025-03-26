package com.youyi.runner.config.assembler;

import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.infra.conf.core.ConfigType;
import com.youyi.infra.conf.util.CommonConfUtil;
import com.youyi.runner.config.model.ConfigCreateRequest;
import com.youyi.runner.config.model.ConfigDeleteRequest;
import com.youyi.runner.config.model.ConfigQueryRequest;
import com.youyi.runner.config.model.ConfigUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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
        CommonConfUtil.class
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
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))")
    })
    ConfigDO toDO(ConfigQueryRequest request);

    @Mappings({
        @Mapping(source = "newConfigValue", target = "configValue"),
        @Mapping(source = "currVersion", target = "version"),
        @Mapping(target = "configDesc", source = "configDesc"),
    })
    ConfigDO toDO(ConfigUpdateRequest request);

    ConfigDO toDO(ConfigDeleteRequest request);
}
