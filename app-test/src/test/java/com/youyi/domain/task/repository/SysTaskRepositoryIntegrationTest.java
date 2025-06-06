package com.youyi.domain.task.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.task.repository.po.SysTaskPO;
import com.youyi.domain.task.type.TaskStatus;
import com.youyi.domain.task.type.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/30
 */
class SysTaskRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    SysTaskRepository sysTaskRepository;

    @Test
    void testInsert() {
        SysTaskPO po = new SysTaskPO();
        po.setTaskId(IdSeqUtil.genSysTaskId());
        po.setTaskType(TaskType.UGC_DELETE_EVENT.name());
        po.setTaskStatus(TaskStatus.INIT.name());
        po.setExtraData(GsonUtil.toJson(SysTaskExtraData.of("1884787890761768960")));
        Assertions.assertDoesNotThrow(() -> sysTaskRepository.insert(po));
    }
}
