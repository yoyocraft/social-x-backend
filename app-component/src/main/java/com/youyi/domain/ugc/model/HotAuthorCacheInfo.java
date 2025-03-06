package com.youyi.domain.ugc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/06
 */
@Getter
@Setter
@AllArgsConstructor
public class HotAuthorCacheInfo {
    private String authorId;
    private double hotScore;
}
