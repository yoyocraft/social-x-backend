package com.youyi.domain.ugc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/25
 */
@Getter
@Setter
@AllArgsConstructor
public class HotUgcCacheInfo {

    private String ugcId;

    private String title;

    private Double hotScore;
}
