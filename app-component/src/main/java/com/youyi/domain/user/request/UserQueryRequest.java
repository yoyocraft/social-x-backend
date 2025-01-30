package com.youyi.domain.user.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/30
 */
@Getter
@Setter
public class UserQueryRequest extends BasePageRequest {

    @SerializedName("cursor")
    private String cursor;

    @SerializedName("userId")
    private String userId;
}
