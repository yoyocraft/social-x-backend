package com.youyi.common.base;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public abstract class BasePageRequest extends BaseRequest {

    /**
     * 分页页码
     */
    @SerializedName("page")
    private Integer page;

    /**
     * 分页大小
     */
    @SerializedName("size")
    private Integer size;
}
