package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.PermissionPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Mapper
public interface PermissionMapper {

    int insert(PermissionPO po);

    int insertBatch(@Param("pos") List<PermissionPO> pos);
}
