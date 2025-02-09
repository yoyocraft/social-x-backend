package com.youyi.domain.task.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
public class SysTaskExtraData {

    private String targetId;

    private SysTaskExtraData(String targetId) {
        this.targetId = targetId;
    }

    public static SysTaskExtraData of(String targetId) {
        return new SysTaskExtraData(targetId);
    }
}
