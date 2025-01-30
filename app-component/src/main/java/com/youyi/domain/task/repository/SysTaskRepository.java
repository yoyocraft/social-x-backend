package com.youyi.domain.task.repository;

import com.youyi.common.exception.AppSystemException;
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
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Repository
@RequiredArgsConstructor
public class SysTaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysTaskRepository.class);

    private final SysTaskMapper sysTaskMapper;

    public void insert(SysTaskPO po) {
        try {
            checkNotNull(po);
            int ret = sysTaskMapper.insert(po);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertBatch(List<SysTaskPO> poList) {
        try {
            checkState(CollectionUtils.isNotEmpty(poList));
            int ret = sysTaskMapper.insertBatch(poList);
            checkState(ret == poList.size());
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public int updateStatus(List<String> taskIds, String taskStatus) {
        try {
            checkState(StringUtils.isNotBlank(taskStatus));
            return sysTaskMapper.updateStatus(taskIds, taskStatus);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<SysTaskPO> queryByTypeAndStatusWithCursor(String taskType, List<String> taskStatus, Long cursor, Integer size) {
        try {
            checkState(StringUtils.isNotBlank(taskType) && CollectionUtils.isNotEmpty(taskStatus) && size > 0);
            return sysTaskMapper.queryByTypeAndStatusWithCursor(taskType, taskStatus, cursor, size);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<SysTaskPO> queryToCompensationTasksWithCursor(String taskType, List<String> taskStatus, Long cursor, Integer size) {
        try {
            checkState(StringUtils.isNotBlank(taskType) && CollectionUtils.isNotEmpty(taskStatus) && size > 0);
            return sysTaskMapper.queryToCompensationTasksWithCursor(taskType, taskStatus, cursor, size);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
