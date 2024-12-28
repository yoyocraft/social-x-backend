package com.youyi.core.user.assembler;

import com.youyi.core.user.domain.UserDO;
import com.youyi.core.user.param.UserAddParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
class UserAssemblerTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddParamToDO() {
        UserAddParam addParam = buildAddParam();
        UserDO userDO = UserAssembler.INSTANCE.toDO(addParam);
        Assertions.assertEquals(addParam.getUsername(), userDO.getName());
        Assertions.assertEquals(addParam.getAge(), userDO.getAge());
    }

    private UserAddParam buildAddParam() {
        UserAddParam addParam = new UserAddParam();
        addParam.setUsername("USERNAME");
        addParam.setAge(18);
        return addParam;
    }
}
