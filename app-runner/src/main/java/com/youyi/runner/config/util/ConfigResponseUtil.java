package com.youyi.runner.config.util;

import com.youyi.common.base.Result;
import com.youyi.common.constant.CommonBizState;
import com.youyi.common.util.GsonUtil;
import com.youyi.core.config.param.ConfigCreateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigResponseUtil.class);

    public static Result<Boolean> createSuccess(ConfigCreateParam param) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("create config ,request:{}, response: {}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> createFail(ConfigCreateParam param, String code, String message, CommonBizState state) {
        Result<Boolean> response = Result.of(Boolean.FALSE, code, message, state.name());
        LOGGER.error("create config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }
}
