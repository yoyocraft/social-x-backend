package com.youyi.core.config.assembler;

import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.param.ConfigUpdateParam;
import com.youyi.core.config.repository.po.ConfigPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/31
 */
public class ConfigAssemblerTest {

    @Test
    void testUpdateParamToDO() {
        ConfigUpdateParam param = new ConfigUpdateParam();
        param.setConfigKey("configKey");
        param.setNewConfigValue("newConfigValue");
        param.setEnv("env");
        param.setVersion(1);

        ConfigDO configDO = ConfigAssembler.CONFIG_ASSEMBLER.toDO(param);

        Assertions.assertNotNull(configDO);
        Assertions.assertEquals("configKey", configDO.getConfigKey());
        Assertions.assertEquals("newConfigValue", configDO.getConfigValue());
        Assertions.assertEquals("env", configDO.getEnv());
        Assertions.assertEquals(1, configDO.getVersion());
    }

    @Test
    void testDOToUpdatePO() {
        ConfigDO configDO = new ConfigDO();

        ConfigPO po = ConfigAssembler.CONFIG_ASSEMBLER.toUpdatePO(configDO);

        Assertions.assertNotNull(po.getGmtModified());
    }
}
