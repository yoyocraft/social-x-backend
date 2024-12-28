package com.youyi.common.base;

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
    private List<T> data;
    private Long cursor;
}
