package com.youyi.core.config.repository.mapper;

import com.youyi.core.config.repository.po.ConfigPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Mapper
public interface ConfigMapper {

    int insert(ConfigPO po);
}
