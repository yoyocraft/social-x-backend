package com.youyi.domain.ugc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Getter
@Setter
public class CommentaryExtraData {

    /**
     * 是否被采纳
     */
    private Boolean adopted;

    /**
     * 是否精选
     */
    private Boolean featured;

    /**
     * 是否敏感
     */
    private Boolean sensitive;
}
