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
public class PageCursorResult<T> {

    @SerializedName("data")
    private List<T> data;

    @SerializedName("cursor")
    private Long cursor;
}
