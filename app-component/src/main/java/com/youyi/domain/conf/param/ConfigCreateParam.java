package com.youyi.domain.conf.param;

import com.google.gson.annotations.SerializedName;
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

    @SerializedName("config_key")
    private String configKey;

    @SerializedName("config_value")
    private String configValue;

    @SerializedName("extra_data")
    private String extraData;
}
