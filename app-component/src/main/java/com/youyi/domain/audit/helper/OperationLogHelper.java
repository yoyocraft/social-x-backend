package com.youyi.domain.audit.helper;

import com.youyi.domain.audit.model.OperationLogDO;
import com.youyi.domain.audit.repository.OperationLogRepository;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Component
@RequiredArgsConstructor
public class OperationLogHelper {

    private final OperationLogRepository operationLogRepository;

    public void recordOperationLog(OperationLogDO operationLogDO) {
        OperationLogPO toSaveOperationLog = operationLogDO.buildToSaveOperationLog();
        operationLogRepository.insert(toSaveOperationLog);
    }

}
