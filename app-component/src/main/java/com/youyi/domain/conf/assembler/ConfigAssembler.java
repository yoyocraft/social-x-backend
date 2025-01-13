package com.youyi.domain.conf.assembler;

import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.domain.conf.request.ConfigCreateRequest;
import com.youyi.domain.conf.request.ConfigDeleteRequest;
import com.youyi.domain.conf.request.ConfigQueryRequest;
import com.youyi.domain.conf.request.ConfigUpdateRequest;
import com.youyi.infra.conf.repository.po.ConfigPO;
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
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigAssembler {

    ConfigAssembler CONFIG_ASSEMBLER = Mappers.getMapper(ConfigAssembler.class);

    ConfigDO toDO(ConfigCreateRequest request);

    @Mappings({
        @Mapping(source = "key", target = "configKey")
    })
    ConfigDO toDO(ConfigQueryRequest request);

    @Mappings({
        @Mapping(source = "newConfigValue", target = "configValue"),
        @Mapping(source = "currVersion", target = "version")
    })
    ConfigDO toDO(ConfigUpdateRequest request);

    ConfigDO toDO(ConfigDeleteRequest request);

    @Mappings({
        @Mapping(source = "id", target = "configId")
    })
    ConfigDO toDO(ConfigPO configPO);

    @Mappings({
        @Mapping(source = "configId", target = "id"),
    })
    ConfigPO toUpdateOrDeletePO(ConfigDO configDO);
}
