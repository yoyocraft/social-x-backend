package com.youyi.runner.auth.configuration;

import cn.dev33.satoken.stp.StpInterface;
import com.youyi.common.type.user.PermissionType;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.helper.PermissionHelper;
import com.youyi.domain.user.model.PermissionDO;
import com.youyi.domain.user.model.UserDO;
import java.util.Collections;
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
    private final UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserDO currentUser = userService.getCurrentUserInfo();
        PermissionDO permissionDO = permissionHelper.queryPermissionByRole(currentUser.getRole());
        if (Objects.isNull(permissionDO)) {
            return Collections.emptyList();
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
