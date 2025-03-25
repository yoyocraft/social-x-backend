package com.youyi.domain.user.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.user.repository.mapper.PermissionMapper;
import com.youyi.domain.user.repository.mapper.RolePermissionMapper;
import com.youyi.domain.user.repository.po.PermissionPO;
import com.youyi.domain.user.repository.po.RolePermissionPO;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(PermissionRepository.class);

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MYSQL;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MYSQL_ERROR;
    }

    public void insertBatchPermissions(List<PermissionPO> permissionPOs) {
        checkState(CollectionUtils.isNotEmpty(permissionPOs));
        int ret = executeWithExceptionHandling(() -> permissionMapper.insertBatch(permissionPOs));
        checkState(ret == permissionPOs.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateRolePermission(RolePermissionPO rolePermissionPO) {
        checkState(Objects.nonNull(rolePermissionPO) && StringUtils.isNotBlank(rolePermissionPO.getRole()));
        RolePermissionPO po = executeWithExceptionHandling(() -> rolePermissionMapper.queryByRole(rolePermissionPO.getRole()));
        if (Objects.isNull(po)) {
            int ret = executeWithExceptionHandling(() -> rolePermissionMapper.insert(rolePermissionPO));
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
            return;
        }
        int ret = executeWithExceptionHandling(() -> rolePermissionMapper.update(rolePermissionPO));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    public RolePermissionPO queryRolePermissionByRole(String role) {
        checkNotNull(role);
        return executeWithExceptionHandling(() -> rolePermissionMapper.queryByRole(role));
    }

}
