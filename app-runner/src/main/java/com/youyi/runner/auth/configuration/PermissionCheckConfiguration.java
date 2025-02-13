package com.youyi.runner.auth.configuration;

import cn.dev33.satoken.stp.StpInterface;
import com.google.common.collect.Lists;
import com.youyi.common.type.user.PermissionType;
import com.youyi.domain.user.helper.PermissionHelper;
import com.youyi.domain.user.helper.UserHelper;
import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
@Configuration
@RequiredArgsConstructor
public class PermissionCheckConfiguration implements StpInterface {

    private final PermissionHelper permissionHelper;
    private final UserHelper userHelper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserDO currentUser = userHelper.getCurrentUser();
        PermissionDO permissionDO = permissionHelper.queryPermissionByRole(currentUser.getRole());
        if (Objects.isNull(permissionDO)) {
            return Lists.newArrayList();
        }
        return permissionDO.getPermissions()
            .stream()
            .map(PermissionType::name)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
