package com.youyi.common.base;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
public class PageResult<T> {

    /**
     * 总数
     */
    @SerializedName("total")
    private final Long total;

    /**
     * 当前页码
     */
    @SerializedName("pageNo")
    private final Long pageNo;

    /**
     * 每页数量
     */
    @SerializedName("pageSize")
    private final Long pageSize;

    /**
     * 数据
     */
    @SerializedName("data")
    private final List<T> data;

    private PageResult(Long total, Long pageNo, Long pageSize, List<T> data) {
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.data = data;
    }

    public static <T> PageResult<T> of(Long total, Long pageNo, Long pageSize, List<T> data) {
        return new PageResult<>(total, pageNo, pageSize, data);
    }
}
