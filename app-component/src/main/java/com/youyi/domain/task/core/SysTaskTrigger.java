package com.youyi.domain.task.core;

import com.google.common.collect.Lists;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.task.repository.SysTaskRepository;
import com.youyi.domain.task.repository.po.SysTaskPO;
import com.youyi.domain.task.type.TaskStatus;
import com.youyi.domain.task.type.TaskType;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.EnumSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class SysTaskTrigger {

    private static final Logger logger = LoggerFactory.getLogger(SysTaskTrigger.class);

    private static final long INIT_TASK_DELAY_INTERVAL = 10 * 60 * 1000L;

    private static final long TASK_EXECUTE_INTERVAL = 30 * 60 * 1000L;

    private static final long COMPENSATION_TASK_DELAY_INTERVAL = 10 * 60 * 1000L;

    private static final long COMPENSATION_TASK_EXECUTE_INTERVAL = 60 * 60 * 1000L;

    private static final EnumSet<TaskType> needHandleTaskType = EnumSet.of(
        TaskType.UGC_DELETE_EVENT,
        TaskType.COMMENTARY_DELETE_EVENT,
        TaskType.UGC_ADOPT_EVENT
    );

    /**
     * 需要补偿的任务状态
     */
    private static final List<String> needToCompensateTaskStatus = List.of(
        TaskStatus.PROCESSING.name()
    );

    private static final List<String> needHandleTaskStatus = List.of(
        TaskStatus.INIT.name(),
        TaskStatus.FAIL.name()
    );

    private final SysTaskRepository sysTaskRepository;
    private final TaskProcessorFactory taskProcessorFactory;

    @Scheduled(initialDelay = INIT_TASK_DELAY_INTERVAL, fixedRate = TASK_EXECUTE_INTERVAL)
    public void trigger() {
        for (TaskType taskType : needHandleTaskType) {
            processNormal(taskType);
        }
    }

    @Scheduled(initialDelay = COMPENSATION_TASK_DELAY_INTERVAL, fixedRate = COMPENSATION_TASK_EXECUTE_INTERVAL)
    public void compensate() {
        for (TaskType taskType : needHandleTaskType) {
            processCompensation(taskType);
        }
    }

    private void processNormal(TaskType taskType) {
        Long cursor = 0L;
        while (true) {
            List<SysTaskPO> tasks = loadNeedHandleTasks(taskType, cursor);
            if (tasks.isEmpty()) {
                break;
            }
            doProcess(tasks);
            cursor = tasks.get(tasks.size() - 1).getId();
        }
    }

    private void processCompensation(TaskType taskType) {
        Long cursor = 0L;
        while (true) {
            List<SysTaskPO> tasks = loadCompensationTasks(taskType, cursor);
            if (tasks.isEmpty()) {
                break;
            }
            doProcess(tasks);
            cursor = tasks.get(tasks.size() - 1).getId();
        }
    }

    private void doProcess(List<SysTaskPO> tasks) {
        List<String> successTaskIds = Lists.newArrayList();
        List<String> failTaskIds = Lists.newArrayList();
        List<String> currProcessingTaskIds = tasks.stream().map(SysTaskPO::getTaskId).toList();
        sysTaskRepository.updateStatus(currProcessingTaskIds, TaskStatus.PROCESSING.name());
        for (SysTaskPO task : tasks) {
            try {
                TaskProcessor taskProcessor = taskProcessorFactory.getTaskProcessor(TaskType.of(task.getTaskType()));
                taskProcessor.process(task.getTaskId(), GsonUtil.fromJson(task.getExtraData(), SysTaskExtraData.class));
                successTaskIds.add(task.getTaskId());
            } catch (Exception e) {
                logger.error("process task error, taskId: {}", task.getTaskId(), e);
                failTaskIds.add(task.getTaskId());
            }
        }
        // 无需考虑事务问题，任务处理支持幂等
        if (CollectionUtils.isNotEmpty(successTaskIds)) {
            sysTaskRepository.updateStatus(successTaskIds, TaskStatus.SUCCESS.name());
        }
        if (CollectionUtils.isNotEmpty(failTaskIds)) {
            sysTaskRepository.updateStatus(failTaskIds, TaskStatus.FAIL.name());
        }
    }

    private List<SysTaskPO> loadNeedHandleTasks(TaskType type, Long cursor) {
        return sysTaskRepository.queryByTypeAndStatusWithCursor(
            type.name(),
            needHandleTaskStatus,
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    private List<SysTaskPO> loadCompensationTasks(TaskType type, Long cursor) {
        return sysTaskRepository.queryToCompensationTasksWithCursor(
            type.name(),
            needToCompensateTaskStatus,
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }
}
