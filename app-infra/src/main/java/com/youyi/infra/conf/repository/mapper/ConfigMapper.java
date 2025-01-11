package com.youyi.infra.conf.repository.mapper;

import com.youyi.infra.conf.repository.po.ConfigPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Mapper
public interface ConfigMapper {

    int insert(ConfigPO po);

    ConfigPO queryByConfigKey(@Param("configKey") String configKey, @Param("includeDeleted") boolean includeDeleted);

    int updateConfigValueAndEnv(ConfigPO po);

    int deleteByConfigKeyAndEnv(ConfigPO po);

    List<ConfigPO> queryAll(@Param("includeDeleted") boolean includeDeleted);

}
