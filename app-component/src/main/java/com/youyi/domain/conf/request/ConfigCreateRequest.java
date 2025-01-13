package com.youyi.domain.conf.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Getter
@Setter
public class ConfigCreateRequest extends BaseRequest {

    @SerializedName("config_key")
    private String configKey;

    @SerializedName("config_value")
    private String configValue;

    @SerializedName("extra_data")
    private String extraData;
}
