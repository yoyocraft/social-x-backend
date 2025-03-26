package com.youyi.domain.audit.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.audit.repository.mapper.OperationLogMapper;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Repository
@RequiredArgsConstructor
public class OperationLogRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogRepository.class);

    private final OperationLogMapper operationLogMapper;

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

    public void insert(OperationLogPO po) {
        checkNotNull(po);
        int ret = executeWithExceptionHandling(() -> operationLogMapper.insert(po));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }
}
