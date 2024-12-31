package com.youyi.core.config.repository.mapper;

import com.youyi.core.config.repository.po.ConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Mapper
public interface ConfigMapper {

    int insert(ConfigPO po);

    ConfigPO queryByConfigKeyAndEnv(@Param("configKey") String configKey, @Param("env") String env, @Param("includeDeleted") boolean includeDeleted);

    int updateConfigValueAndEnv(ConfigPO po);

}
