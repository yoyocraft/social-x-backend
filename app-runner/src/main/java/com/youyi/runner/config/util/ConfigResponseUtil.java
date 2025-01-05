package com.youyi.runner.config.util;

import com.youyi.common.base.Result;
import com.youyi.common.type.CommonBizState;
import com.youyi.common.util.GsonUtil;
import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.param.ConfigCreateParam;
import com.youyi.core.config.param.ConfigDeleteParam;
import com.youyi.core.config.param.ConfigQueryParam;
import com.youyi.core.config.param.ConfigUpdateParam;
import com.youyi.runner.config.model.ConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.config.util.ConfigConverter.CONFIG_CONVERTER;

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

    public static Result<ConfigVO> querySuccess(ConfigDO configDO, ConfigQueryParam param) {
        Result<ConfigVO> response = Result.success(CONFIG_CONVERTER.toVO(configDO));
        LOGGER.info("query config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<ConfigVO> queryFail(ConfigQueryParam param, String code, String message, CommonBizState state) {
        Result<ConfigVO> response = Result.of(null, code, message, state.name());
        LOGGER.error("query config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> updateSuccess(ConfigUpdateParam param) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("update config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> updateFail(ConfigUpdateParam param, String code, String message, CommonBizState state) {
        Result<Boolean> response = Result.of(Boolean.FALSE, code, message, state.name());
        LOGGER.error("update config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> deleteSuccess(ConfigDeleteParam param) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("delete config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> deleteFail(ConfigDeleteParam param, String code, String message, CommonBizState state) {
        Result<Boolean> response = Result.of(Boolean.FALSE, code, message, state.name());
        LOGGER.error("delete config, request:{}, response:{}", GsonUtil.toJson(param), GsonUtil.toJson(response));
        return response;
    }
}
