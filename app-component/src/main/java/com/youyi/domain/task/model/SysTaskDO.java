package com.youyi.domain.task.model;

import com.youyi.common.type.task.TaskStatus;
import com.youyi.common.type.task.TaskType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.task.repository.po.SysTaskPO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
public class SysTaskDO {

    private String taskId;
    private TaskType taskType;
    private TaskStatus taskStatus;
    private SysTaskExtraData extraData;

    public void create() {
        this.taskId = IdSeqUtil.genSysTaskId();
        this.taskStatus = TaskStatus.INIT;
    }

    public SysTaskPO buildToSaveSysTaskPO() {
        SysTaskPO po = new SysTaskPO();
        po.setTaskId(this.taskId);
        po.setTaskType(this.taskType.name());
        po.setTaskStatus(this.taskStatus.name());
        po.setExtraData(GsonUtil.toJson(this.extraData));
        return po;
    }
}
