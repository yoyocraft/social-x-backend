package com.youyi.runner.user.assembler;

import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.type.PermissionType;
import com.youyi.domain.user.type.UserRoleType;
import com.youyi.runner.user.model.PermissionAddRequest;
import com.youyi.runner.user.model.RolePermissionAuthorizeRequest;
import com.youyi.runner.user.model.RolePermissionRevokeRequest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
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

    default List<PermissionType> toPermissions(List<String> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return List.of();
        }

        return permissions.stream().map(PermissionType::of).toList();
    }
}
