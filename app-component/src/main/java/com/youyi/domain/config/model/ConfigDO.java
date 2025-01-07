package com.youyi.domain.config.model;

import com.youyi.infra.config.repository.po.ConfigPO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.youyi.common.constant.RepositoryConstant.INIT_VERSION;
import static com.youyi.common.type.Env.LOCAL;

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
    private String env;
    private Integer version;
    private String extraData;
    private Long deletedAt;

    private ConfigPO toSaveConfig;

    public void create() {
        setEnvIfNeeded();
    }

    public void buildToSaveConfig() {
        toSaveConfig = new ConfigPO();
        toSaveConfig.setConfigKey(configKey);
        toSaveConfig.setConfigValue(configValue);
        toSaveConfig.setEnv(env);
        toSaveConfig.setVersion(INIT_VERSION);
        toSaveConfig.setGmtCreate(System.currentTimeMillis());
        toSaveConfig.setGmtModified(System.currentTimeMillis());
        toSaveConfig.setExtraData(extraData);
    }

    public void preUpdate() {
        setEnvIfNeeded();
    }

    public void preDelete() {
        setEnvIfNeeded();
        this.deletedAt = System.currentTimeMillis();
    }

    private void setEnvIfNeeded() {
        if (StringUtils.isBlank(env)) {
            env = LOCAL.name();
        }
    }
}
