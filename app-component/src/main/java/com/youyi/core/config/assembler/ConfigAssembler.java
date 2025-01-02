package com.youyi.core.config.assembler;

import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.param.ConfigCreateParam;
import com.youyi.core.config.param.ConfigDeleteParam;
import com.youyi.core.config.param.ConfigQueryParam;
import com.youyi.core.config.param.ConfigUpdateParam;
import com.youyi.core.config.repository.po.ConfigPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigAssembler {

    ConfigAssembler CONFIG_ASSEMBLER = Mappers.getMapper(ConfigAssembler.class);

    ConfigDO toDO(ConfigCreateParam param);

    @Mappings({
        @Mapping(source = "key", target = "configKey")
    })
    ConfigDO toDO(ConfigQueryParam param);

    @Mappings({
        @Mapping(source = "newConfigValue", target = "configValue"),
        @Mapping(source = "currVersion", target = "version")
    })
    ConfigDO toDO(ConfigUpdateParam param);

    ConfigDO toDO(ConfigDeleteParam param);

    @Mappings({
        @Mapping(source = "id", target = "configId")
    })
    ConfigDO toDO(ConfigPO configPO);

    @Mappings({
        @Mapping(source = "configId", target = "id"),
        @Mapping(target = "gmtModified", expression = "java(System.currentTimeMillis())")
    })
    ConfigPO toUpdateOrDeletePO(ConfigDO configDO);
}
