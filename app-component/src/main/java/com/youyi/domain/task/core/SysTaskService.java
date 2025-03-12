package com.youyi.domain.task.core;

import com.youyi.common.type.task.TaskType;
import com.youyi.domain.task.model.SysTaskDO;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.task.repository.SysTaskRepository;
import com.youyi.domain.task.repository.po.SysTaskPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class SysTaskService {

    private final SysTaskRepository sysTaskRepository;

    public void saveCommonSysTask(String targetId, TaskType taskType) {
        SysTaskDO sysTaskDO = new SysTaskDO();
        sysTaskDO.create();
        sysTaskDO.setTaskType(taskType);
        sysTaskDO.setExtraData(SysTaskExtraData.of(targetId));

        SysTaskPO po = sysTaskDO.buildToSaveSysTaskPO();
        sysTaskRepository.insert(po);
    }

    public void saveBatchCommonSysTask(List<String> targetIds, TaskType taskType) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return;
        }
        List<SysTaskPO> sysTaskPOList = targetIds.stream()
            .map(targetId -> {
                    SysTaskDO sysTaskDO = new SysTaskDO();
                    sysTaskDO.create();
                    sysTaskDO.setTaskType(taskType);
                    sysTaskDO.setExtraData(SysTaskExtraData.of(targetId));
                    return sysTaskDO.buildToSaveSysTaskPO();
                }
            ).toList();
        sysTaskRepository.insertBatch(sysTaskPOList);
    }

}
