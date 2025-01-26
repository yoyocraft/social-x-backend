package com.youyi.domain.user.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.youyi.common.type.user.PermissionType;
import com.youyi.common.type.user.UserRoleType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.repository.po.PermissionPO;
import com.youyi.domain.user.repository.po.RolePermissionPO;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Getter
@Setter
public class PermissionDO {

    private UserRoleType role;

    private List<PermissionType> permissions;

    private PermissionType toSavePermission;

    private List<PermissionType> toSavePermissions;

    @Deprecated
    public PermissionPO buildToSavePermissionPO() {
        PermissionPO po = new PermissionPO();
        po.setPermissionName(toSavePermission.name());
        return po;
    }

    public List<PermissionPO> buildToSavePermissionPOs() {
        return toSavePermissions.stream()
            .map(this::buildPermissionPO)
            .collect(Collectors.toList());
    }

    public RolePermissionPO buildToSaveRolePermissionPO() {
        RolePermissionPO po = new RolePermissionPO();
        po.setRole(role.name());
        po.setPermissions(GsonUtil.toJson(permissions));
        return po;
    }

    public void mergePermission(RolePermissionPO rolePermissionPO) {
        if (Objects.isNull(rolePermissionPO)) {
            return;
        }

        String permissionsJson = rolePermissionPO.getPermissions();
        List<PermissionType> permissionsFromPO = GsonUtil.fromJson(permissionsJson, List.class, PermissionType.class);

        // 加入到 permissions 并去重
        Set<PermissionType> finalPermissions = Sets.newHashSet(permissions);
        finalPermissions.addAll(permissionsFromPO);

        this.permissions = Lists.newArrayList(finalPermissions);
    }

    public void diffPermissionForRevoke(RolePermissionPO rolePermissionPO) {
        if (Objects.isNull(rolePermissionPO)) {
            return;
        }
        String permissionsJson = rolePermissionPO.getPermissions();
        List<PermissionType> basePermissions = GsonUtil.fromJson(permissionsJson, List.class, PermissionType.class);
        // 去掉 basePermissions 中的 permissions
        Set<PermissionType> finalPermissions = Sets.difference(Sets.newHashSet(basePermissions), Sets.newHashSet(permissions));
        this.permissions = Lists.newArrayList(finalPermissions);
    }

    private PermissionPO buildPermissionPO(PermissionType permission) {
        PermissionPO po = new PermissionPO();
        po.setPermissionName(permission.name());
        return po;
    }
}
