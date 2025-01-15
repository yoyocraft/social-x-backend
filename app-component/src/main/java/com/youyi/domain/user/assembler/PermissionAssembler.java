package com.youyi.domain.user.assembler;

import com.google.common.collect.Lists;
import com.youyi.common.type.user.PermissionType;
import com.youyi.common.type.user.UserRoleType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.repository.po.RolePermissionPO;
import com.youyi.domain.user.request.PermissionAddRequest;
import com.youyi.domain.user.request.RolePermissionAuthorizeRequest;
import com.youyi.domain.user.request.RolePermissionRevokeRequest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/14
 */
@Mapper(
    imports = {
        UserRoleType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PermissionAssembler {

    PermissionAssembler PERMISSION_ASSEMBLER = Mappers.getMapper(PermissionAssembler.class);

    @Mappings({
        @Mapping(target = "toSavePermissions", expression = "java(toPermissions(request.getPermissions()))")
    })
    PermissionDO toPermissionDO(PermissionAddRequest request);

    @Mappings({
        @Mapping(target = "role", expression = "java(UserRoleType.of(request.getRole()))"),
        @Mapping(target = "permissions", expression = "java(toPermissions(request.getGrantedPermissions()))")
    })
    PermissionDO toPermissionDO(RolePermissionAuthorizeRequest request);

    @Mappings({
        @Mapping(target = "role", expression = "java(UserRoleType.of(request.getRole()))"),
        @Mapping(target = "permissions", expression = "java(toPermissions(request.getRevokePermissions()))")
    })
    PermissionDO toPermissionDO(RolePermissionRevokeRequest request);

    @Mappings({
        @Mapping(target = "permissions", expression = "java(toPermissions(po))")
    })
    PermissionDO toPermissionDO(RolePermissionPO po);

    default String toSavePermissions(PermissionDO permissionDO) {
        List<PermissionType> permissions = permissionDO.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return StringUtils.EMPTY;
        }

        List<String> permissionNames = permissions.stream().map(PermissionType::getValue).toList();

        return GsonUtil.toJson(permissionNames);
    }

    default List<PermissionType> toPermissions(RolePermissionPO po) {
        String permissions = po.getPermissions();
        if (StringUtils.isBlank(permissions)) {
            return Lists.newArrayList();
        }

        return GsonUtil.fromJson(permissions, List.class, PermissionType.class);
    }

    default List<PermissionType> toPermissions(List<String> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return Lists.newArrayList();
        }

        return permissions.stream().map(PermissionType::of).toList();
    }
}
