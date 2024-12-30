package com.youyi.core.config.assembler;

import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.param.ConfigCreateParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Mapper
public interface ConfigAssembler {

    ConfigAssembler INSTANCE = Mappers.getMapper(ConfigAssembler.class);

    ConfigDO toDO(ConfigCreateParam param);

}
