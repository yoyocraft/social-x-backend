package com.youyi.common.base;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@AllArgsConstructor
public class PageResult<T> {

    @SerializedName("total")
    private Long total;

    @SerializedName("pageNo")
    private Long pageNo;

    @SerializedName("pageSize")
    private Long pageSize;

    @SerializedName("data")
    private List<T> data;

    public static <T> PageResult<T> of(Long total, Long pageNo, Long pageSize, List<T> data) {
        return new PageResult<>(total, pageNo, pageSize, data);
    }
}
