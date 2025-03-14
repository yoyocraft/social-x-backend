package com.youyi.domain.user.helper;

import com.youyi.domain.user.type.UserRoleType;
import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.repository.PermissionRepository;
import com.youyi.domain.user.repository.po.PermissionPO;
import com.youyi.domain.user.repository.po.RolePermissionPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Service
@RequiredArgsConstructor
public class PermissionHelper {

    private final PermissionRepository permissionRepository;

    public void addPermission(PermissionDO permissionDO) {
        List<PermissionPO> pos = permissionDO.buildToSavePermissionPOs();
        permissionRepository.insertBatchPermissions(pos);
    }

    public void authorize(PermissionDO permissionDO) {
        // 1. 查询角色拥有的权限
        RolePermissionPO poFromDB = permissionRepository.queryRolePermissionByRole(permissionDO.getRole().name());
        // 2. 合并权限，去重
        permissionDO.mergePermission(poFromDB);
        // 3. 保存角色权限
        RolePermissionPO po = permissionDO.buildToSaveRolePermissionPO();
        permissionRepository.insertOrUpdateRolePermission(po);
    }

    public void revoke(PermissionDO permissionDO) {
        // 1. 查询用户拥有的权限
        RolePermissionPO poFromDB = permissionRepository.queryRolePermissionByRole(permissionDO.getRole().name());
        // 2. 求交集
        permissionDO.diffPermissionForRevoke(poFromDB);
        // 3. 保存用户权限
        RolePermissionPO po = permissionDO.buildToSaveRolePermissionPO();
        permissionRepository.insertOrUpdateRolePermission(po);
    }

    public PermissionDO queryPermissionByRole(UserRoleType role) {
        RolePermissionPO po = permissionRepository.queryRolePermissionByRole(role.name());
        PermissionDO permissionDO = new PermissionDO();
        permissionDO.fillWithRolePermissionPO(po);
        return permissionDO;
    }
}
