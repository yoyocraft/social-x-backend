package com.youyi.domain.task.model;

import com.youyi.common.util.GsonUtil;
import com.youyi.domain.task.repository.po.SysTaskPO;
import com.youyi.domain.task.type.TaskStatus;
import com.youyi.domain.task.type.TaskType;
import lombok.Getter;
import lombok.Setter;

import static com.youyi.common.util.seq.IdSeqUtil.genSysTaskId;

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
    private Long gmtCreate;
    private Long gmtModified;

    public void create() {
        this.taskId = genSysTaskId();
        this.taskStatus = TaskStatus.INIT;
        this.gmtCreate = System.currentTimeMillis();
        this.gmtModified = System.currentTimeMillis();
    }

    public SysTaskPO buildToSaveSysTaskPO() {
        SysTaskPO po = new SysTaskPO();
        po.setTaskId(this.taskId);
        po.setTaskType(this.taskType.name());
        po.setTaskStatus(this.taskStatus.name());
        po.setExtraData(GsonUtil.toJson(this.extraData));
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }
}
