package com.youyi.common.base;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/25
 */
class BaseRepositoryTest {

    static BaseRepository repository;

    @BeforeAll
    static void setUp() {
        repository = new TestRepository();
    }

    @Test
    void testExecuteWithExceptionHandling() {
        AppSystemException ex = Assertions.assertThrows(AppSystemException.class, () -> repository.executeWithExceptionHandling(this::errorMethod));
        Assertions.assertNotNull(ex);
        Assertions.assertEquals(InfraCode.INFRA_SUCCESS.getCode(), ex.getCode());
    }

    void errorMethod() {
        throw new RuntimeException("test");
    }

    static class TestRepository extends BaseRepository {
        private static final Logger logger = LoggerFactory.getLogger(TestRepository.class);

        @Override
        protected Logger getLogger() {
            return logger;
        }

        @Override
        protected InfraType getInfraType() {
            return InfraType.UNKNOWN;
        }

        @Override
        protected InfraCode getInfraCode() {
            return InfraCode.INFRA_SUCCESS;
        }
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme