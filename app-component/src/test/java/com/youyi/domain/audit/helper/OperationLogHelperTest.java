package com.youyi.domain.audit.helper;

import com.youyi.common.type.OperationType;
import com.youyi.domain.audit.model.OperationLogDO;
import com.youyi.domain.audit.repository.OperationLogRepository;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/13
 */
class OperationLogHelperTest {
    @Mock
    OperationLogRepository operationLogRepository;
    @InjectMocks
    OperationLogHelper operationLogHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecordOperationLog() {
        operationLogHelper.recordOperationLog(buildDO());
        verify(operationLogRepository).insert(any(OperationLogPO.class));
    }

    OperationLogDO buildDO() {
        OperationLogDO operationLogDO = new OperationLogDO();
        operationLogDO.setOperationType(OperationType.UNKNOWN);
        operationLogDO.setOperatorId("operatorId");
        operationLogDO.setOperatorName("operatorName");
        return operationLogDO;
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme