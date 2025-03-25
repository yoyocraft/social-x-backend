package com.youyi.domain.task.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.task.repository.mapper.SysTaskMapper;
import com.youyi.domain.task.repository.po.SysTaskPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Repository
@RequiredArgsConstructor
public class SysTaskRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(SysTaskRepository.class);

    private final SysTaskMapper sysTaskMapper;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MYSQL;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MYSQL_ERROR;
    }

    public void insert(SysTaskPO po) {
        checkNotNull(po);
        int ret = executeWithExceptionHandling(() -> sysTaskMapper.insert(po));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    public void insertBatch(List<SysTaskPO> poList) {
        checkState(CollectionUtils.isNotEmpty(poList));
        int ret = executeWithExceptionHandling(() -> sysTaskMapper.insertBatch(poList));
        checkState(ret == poList.size());
    }

    public void updateStatus(List<String> taskIds, String taskStatus) {
        checkState(StringUtils.isNotBlank(taskStatus));
        executeWithExceptionHandling(() -> sysTaskMapper.updateStatus(taskIds, taskStatus));
    }

    public List<SysTaskPO> queryByTypeAndStatusWithCursor(String taskType, List<String> taskStatus, Long cursor, Integer size) {
        checkState(StringUtils.isNotBlank(taskType) && CollectionUtils.isNotEmpty(taskStatus) && size > 0);
        return executeWithExceptionHandling(() -> sysTaskMapper.queryByTypeAndStatusWithCursor(taskType, taskStatus, cursor, size, Boolean.FALSE));
    }

    public List<SysTaskPO> queryToCompensationTasksWithCursor(String taskType, List<String> taskStatus, Long cursor, Integer size) {
        checkState(StringUtils.isNotBlank(taskType) && CollectionUtils.isNotEmpty(taskStatus) && size > 0);
        return executeWithExceptionHandling(() -> sysTaskMapper.queryByTypeAndStatusWithCursor(taskType, taskStatus, cursor, size, Boolean.TRUE));

    }
}
