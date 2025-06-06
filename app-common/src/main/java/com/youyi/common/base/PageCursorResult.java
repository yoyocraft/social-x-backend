package com.youyi.common.base;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
public class PageCursorResult<CURSOR, T> {

    /**
     * 数据
     */
    @SerializedName("data")
    private final List<T> data;

    /**
     * 游标
     */
    @SerializedName("cursor")
    private final CURSOR cursor;

    /**
     * 是否有更多
     */
    @SerializedName("hasMore")
    private final Boolean hasMore;

    private PageCursorResult(List<T> data, CURSOR cursor, Boolean hasMore) {
        this.data = data;
        this.cursor = cursor;
        this.hasMore = hasMore;
    }

    public static <CURSOR, T> PageCursorResult<CURSOR, T> of(List<T> data, CURSOR cursor, Boolean hasMore) {
        return new PageCursorResult<>(data, cursor, hasMore);
    }
}
