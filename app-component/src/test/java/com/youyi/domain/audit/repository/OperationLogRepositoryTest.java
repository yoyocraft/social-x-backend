package com.youyi.domain.audit.repository;

import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.audit.repository.mapper.OperationLogMapper;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/13
 */
class OperationLogRepositoryTest {
    @Mock
    OperationLogMapper operationLogMapper;
    @InjectMocks
    OperationLogRepository operationLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInfraType() {
        InfraType result = operationLogRepository.getInfraType();
        Assertions.assertEquals(InfraType.MYSQL, result);
    }

    @Test
    void testGetInfraCode() {
        InfraCode result = operationLogRepository.getInfraCode();
        Assertions.assertEquals(InfraCode.MYSQL_ERROR, result);
    }

    @Test
    void testInsert() {
        Assertions.assertThrows(NullPointerException.class, () -> operationLogRepository.insert(null));

        when(operationLogMapper.insert(any(OperationLogPO.class))).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> operationLogRepository.insert(new OperationLogPO()));
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme