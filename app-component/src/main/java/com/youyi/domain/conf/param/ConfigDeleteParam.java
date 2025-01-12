package com.youyi.domain.conf.param;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/31
 */
@Getter
@Setter
public class ConfigDeleteParam extends BaseParam {

    @SerializedName("config_key")
    private String configKey;
}
