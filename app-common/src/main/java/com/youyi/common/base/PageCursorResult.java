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

    @SerializedName("list")
    private final List<T> list;

    @SerializedName("cursor")
    private final CURSOR cursor;

    private PageCursorResult(List<T> list, CURSOR cursor) {
        this.list = list;
        this.cursor = cursor;
    }

    public static <CURSOR, T> PageCursorResult<CURSOR, T> of(List<T> list, CURSOR cursor) {
        return new PageCursorResult<>(list, cursor);
    }
}
