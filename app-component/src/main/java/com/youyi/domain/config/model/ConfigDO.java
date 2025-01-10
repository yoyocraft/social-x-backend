package com.youyi.domain.config.model;

import com.youyi.infra.config.repository.po.ConfigPO;
import lombok.Getter;
import lombok.Setter;

import static com.youyi.common.constant.RepositoryConstant.INIT_VERSION;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Getter
@Setter
public class ConfigDO {

    private Long configId;
    private String configKey;
    private String configValue;
    private Integer version;
    private String extraData;
    private Long deletedAt;

    public ConfigPO buildToSaveConfig() {
        ConfigPO toSaveConfig = new ConfigPO();
        toSaveConfig.setConfigKey(configKey);
        toSaveConfig.setConfigValue(configValue);
        toSaveConfig.setVersion(INIT_VERSION);
        toSaveConfig.setExtraData(extraData);
        return toSaveConfig;
    }

    public void preDelete() {
        this.deletedAt = System.currentTimeMillis();
    }
}
