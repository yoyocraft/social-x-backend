package com.youyi.runner.config.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@Getter
@Setter
public class ConfigInfoResponse extends BaseResponse {

    @SerializedName("config_id")
    private Long configId;

    @SerializedName("config_key")
    private String configKey;

    @SerializedName("config_value")
    private String configValue;

    @SerializedName("extra_data")
    private Integer version;
}
