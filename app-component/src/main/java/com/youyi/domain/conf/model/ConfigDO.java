package com.youyi.domain.conf.model;

import com.youyi.infra.conf.repository.po.ConfigPO;
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

    public ConfigPO buildToUpdateOrDeletePO() {
        ConfigPO toUpdateConfig = new ConfigPO();
        toUpdateConfig.setId(configId);
        toUpdateConfig.setVersion(version);
        toUpdateConfig.setConfigKey(configKey);
        toUpdateConfig.setConfigValue(configValue);
        toUpdateConfig.setExtraData(extraData);
        toUpdateConfig.setDeletedAt(deletedAt);
        return toUpdateConfig;
    }

    public void delete() {
        this.deletedAt = System.currentTimeMillis();
    }

    public void fillWithConfigPO(ConfigPO configPO) {
        this.configId = configPO.getId();
        this.configKey = configPO.getConfigKey();
        this.configValue = configPO.getConfigValue();
        this.version = configPO.getVersion();
        this.extraData = configPO.getExtraData();
        this.deletedAt = configPO.getDeletedAt();
    }
}
