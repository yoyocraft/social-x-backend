package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.RolePermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Mapper
public interface RolePermissionMapper {
    
    RolePermissionPO queryByRole(@Param("role") String role);

    int insert(RolePermissionPO po);

    int update(RolePermissionPO po);
}
