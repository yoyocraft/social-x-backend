package com.youyi.domain.task.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
public class SysTaskPO extends BasePO {

    private String taskId;

    private String taskType;

    private String taskStatus;
}
