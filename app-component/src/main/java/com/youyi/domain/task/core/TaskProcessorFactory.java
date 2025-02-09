package com.youyi.domain.task.core;

import com.youyi.common.type.task.TaskType;
import com.youyi.domain.ugc.processor.CommentaryDeleteProcessor;
import com.youyi.domain.ugc.processor.UgcAdoptProcessor;
import com.youyi.domain.ugc.processor.UgcDeleteProcessor;
import java.util.EnumMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class TaskProcessorFactory implements SmartInitializingSingleton {

    private static EnumMap<TaskType, TaskProcessor> taskProcessorMap;

    private final UgcDeleteProcessor ugcDeleteProcessor;
    private final CommentaryDeleteProcessor commentaryDeleteProcessor;
    private final UgcAdoptProcessor ugcAdoptProcessor;

    public TaskProcessor getTaskProcessor(TaskType taskType) {
        return taskProcessorMap.get(taskType);
    }

    @Override
    public void afterSingletonsInstantiated() {
        taskProcessorMap = new EnumMap<>(TaskType.class);
        taskProcessorMap.put(TaskType.UGC_DELETE_EVENT, ugcDeleteProcessor);
        taskProcessorMap.put(TaskType.COMMENTARY_DELETE_EVENT, commentaryDeleteProcessor);
        taskProcessorMap.put(TaskType.UGC_ADOPT_EVENT, ugcAdoptProcessor);
    }
}
