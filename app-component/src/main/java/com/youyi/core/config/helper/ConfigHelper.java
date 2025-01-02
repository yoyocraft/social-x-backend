package com.youyi.core.config.helper;

import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.repository.ConfigRepository;
import com.youyi.core.config.repository.po.ConfigPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.youyi.core.config.assembler.ConfigAssembler.CONFIG_ASSEMBLER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Service
@RequiredArgsConstructor
public class ConfigHelper {

    private final ConfigRepository configRepository;

    public void createConfig(ConfigDO configDO) {
        configDO.create();
        configDO.buildToSaveConfig();
        configRepository.insert(configDO.getToSaveConfig());
    }

    public ConfigDO queryConfig(ConfigDO configDO) {
        ConfigPO configPO = configRepository.queryByConfigKeyAndEnv(configDO.getConfigKey(), configDO.getEnv());
        return CONFIG_ASSEMBLER.toDO(configPO);
    }

    public void updateConfig(ConfigDO configDO) {
        configDO.preUpdate();
        ConfigPO configPO = CONFIG_ASSEMBLER.toUpdateOrDeletePO(configDO);
        configRepository.updateConfigValueAndEnv(configPO);
    }

    public void deleteConfig(ConfigDO configDO) {
        configDO.preDelete();
        ConfigPO configPO = CONFIG_ASSEMBLER.toUpdateOrDeletePO(configDO);
        configRepository.deleteByConfigKeyAndEnv(configPO);
    }
}
