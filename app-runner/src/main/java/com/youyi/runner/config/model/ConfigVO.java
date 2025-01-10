package com.youyi.runner.config.model;

import com.youyi.common.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Getter
@Setter
public class ConfigVO extends BaseVO {

    private Long configId;
    private String configKey;
    private String configValue;
    private Integer version;
}
