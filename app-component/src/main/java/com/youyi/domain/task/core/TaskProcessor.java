package com.youyi.domain.task.core;

import com.youyi.domain.task.model.SysTaskExtraData;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
public interface TaskProcessor {

    void process(String taskId, SysTaskExtraData extraData);
}
