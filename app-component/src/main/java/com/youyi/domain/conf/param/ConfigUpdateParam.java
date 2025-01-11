package com.youyi.domain.conf.param;

import com.youyi.common.base.BaseParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Getter
@Setter
public class ConfigUpdateParam extends BaseParam {

    private String configKey;
    private String newConfigValue;
    private Integer currVersion;
}
