package com.youyi.domain.user.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.user.repository.mapper.PermissionMapper;
import com.youyi.domain.user.repository.mapper.RolePermissionMapper;
import com.youyi.domain.user.repository.po.PermissionPO;
import com.youyi.domain.user.repository.po.RolePermissionPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionRepository.class);

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    public void insertPermission(PermissionPO permissionPO) {
        try {
            checkNotNull(permissionPO);
            int ret = permissionMapper.insert(permissionPO);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertBatchPermissions(List<PermissionPO> permissionPOs) {
        try {
            checkState(CollectionUtils.isNotEmpty(permissionPOs));
            int ret = permissionMapper.insertBatch(permissionPOs);
            checkState(ret == permissionPOs.size());
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertOrUpdateRolePermission(RolePermissionPO rolePermissionPO) {
        try {
            checkNotNull(rolePermissionPO);
            int ret = rolePermissionMapper.insertOrUpdate(rolePermissionPO);
            checkState(ret >= SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public RolePermissionPO queryRolePermissionByRole(String role) {
        try {
            checkNotNull(role);
            return rolePermissionMapper.queryByRole(role);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

}
