package com.youyi.runner.config.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.domain.conf.request.ConfigCreateRequest;
import com.youyi.domain.conf.request.ConfigDeleteRequest;
import com.youyi.domain.conf.request.ConfigQueryRequest;
import com.youyi.domain.conf.request.ConfigUpdateRequest;
import com.youyi.runner.config.model.ConfigInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.config.util.ConfigConverter.CONFIG_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigResponseUtil.class);

    public static Result<Boolean> createSuccess(ConfigCreateRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("create config, request:{}, response: {}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<ConfigInfoResponse> querySuccess(ConfigDO configDO, ConfigQueryRequest request) {
        Result<ConfigInfoResponse> response = Result.success(CONFIG_CONVERTER.toResponse(configDO));
        LOGGER.info("query config, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> updateSuccess(ConfigUpdateRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("update config, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> deleteSuccess(ConfigDeleteRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("delete config, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
