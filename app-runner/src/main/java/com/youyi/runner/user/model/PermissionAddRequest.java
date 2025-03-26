package com.youyi.runner.user.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/15
 */
@Getter
@Setter
public class PermissionAddRequest extends BaseRequest {

    @SerializedName("permissions")
    private List<String> permissions;
}
