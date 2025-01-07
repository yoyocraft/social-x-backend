package com.youyi.runner.config.util;

import com.youyi.domain.config.model.ConfigDO;
import com.youyi.runner.config.model.ConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/31
 */
@Mapper
public interface ConfigConverter {

    ConfigConverter CONFIG_CONVERTER = Mappers.getMapper(ConfigConverter.class);

    ConfigVO toVO(ConfigDO configDO);
}
