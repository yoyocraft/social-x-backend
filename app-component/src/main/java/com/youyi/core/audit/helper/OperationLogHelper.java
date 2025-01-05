package com.youyi.core.audit.helper;

import com.youyi.core.audit.domain.OperationLogDO;
import com.youyi.core.audit.repository.OperationLogRepository;
import com.youyi.core.audit.repository.po.OperationLogPO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.youyi.core.audit.assembler.OperationLogAssembler.OPERATION_LOG_ASSEMBLER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Component
@RequiredArgsConstructor
public class OperationLogHelper {

    private final OperationLogRepository operationLogRepository;

    public void recordOperationLog(OperationLogDO operationLogDO) {
        operationLogDO.buildToSaveOperationLog();
        operationLogRepository.insert(operationLogDO.getToSaveOperationLog());
    }

    public List<OperationLogDO> queryByTypeAndOperatorId(String operationType, Long operatorId) {
        List<OperationLogPO> opLogPos = operationLogRepository.queryByTypeAndOperatorId(operationType, operatorId);
        return opLogPos.stream()
            .map(OPERATION_LOG_ASSEMBLER::toDO)
            .collect(Collectors.toList());
    }
}
