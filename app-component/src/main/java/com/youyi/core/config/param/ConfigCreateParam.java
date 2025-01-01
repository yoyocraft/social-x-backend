package com.youyi.core.config.param;

import com.youyi.common.base.BaseParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Getter
@Setter
public class ConfigCreateParam extends BaseParam {

    private String configKey;
    private String configValue;
    private String env;
    private String extraData;
}
