package com.youyi.runner.config.controller;

import com.youyi.common.base.Result;
import com.youyi.common.constant.CommonBizState;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ServerType;
import com.youyi.common.util.GsonUtil;
import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.helper.ConfigHelper;
import com.youyi.core.config.param.ConfigCreateParam;
import com.youyi.core.config.param.ConfigQueryParam;
import com.youyi.core.config.param.ConfigUpdateParam;
import com.youyi.runner.config.model.ConfigVO;
import com.youyi.runner.config.util.ConfigValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.constant.ErrorCodeConstant.SYSTEM_ERROR_RETRY_LATER;
import static com.youyi.common.constant.ErrorCodeConstant.SYSTEM_ERROR_RETRY_LATER_MESSAGE;
import static com.youyi.common.util.LogUtil.serverExpLog;
import static com.youyi.core.config.assembler.ConfigAssembler.CONFIG_ASSEMBLER;
import static com.youyi.runner.config.util.ConfigResponseUtil.createFail;
import static com.youyi.runner.config.util.ConfigResponseUtil.createSuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.queryFail;
import static com.youyi.runner.config.util.ConfigResponseUtil.querySuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.updateFail;
import static com.youyi.runner.config.util.ConfigResponseUtil.updateSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    private final ConfigHelper configHelper;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Boolean> createConfig(@RequestBody ConfigCreateParam param) {
        try {
            ConfigValidator.validateConfigCreateParam(param);
            ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
            configHelper.createConfig(configDO);
            return createSuccess(param);
        } catch (AppBizException e) {
            return createFail(param, e.getCode(), e.getMessage(), CommonBizState.FAILED);
        } catch (Exception e) {
            serverExpLog(LOGGER, ServerType.HTTP, "createConfig", GsonUtil.toJson(param), e);
            return createFail(param, SYSTEM_ERROR_RETRY_LATER, SYSTEM_ERROR_RETRY_LATER_MESSAGE, CommonBizState.UNKNOWN);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result<ConfigVO> queryConfig(ConfigQueryParam param) {
        try {
            ConfigValidator.validateConfigQueryParam(param);
            ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
            ConfigDO config = configHelper.queryConfig(configDO);
            return querySuccess(config, param);
        } catch (AppBizException e) {
            return queryFail(param, e.getCode(), e.getMessage(), CommonBizState.FAILED);
        } catch (Exception e) {
            serverExpLog(LOGGER, ServerType.HTTP, "queryConfig", GsonUtil.toJson(param), e);
            return queryFail(param, SYSTEM_ERROR_RETRY_LATER, SYSTEM_ERROR_RETRY_LATER_MESSAGE, CommonBizState.UNKNOWN);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Result<Boolean> updateConfig(@RequestBody ConfigUpdateParam param) {
        try {
            ConfigValidator.validateConfigUpdateParam(param);
            ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
            configHelper.updateConfig(configDO);
            return updateSuccess(param);
        } catch (AppBizException e) {
            return updateFail(param, e.getCode(), e.getMessage(), CommonBizState.FAILED);
        } catch (Exception e) {
            serverExpLog(LOGGER, ServerType.HTTP, "updateConfig", GsonUtil.toJson(param), e);
            return updateFail(param, SYSTEM_ERROR_RETRY_LATER, SYSTEM_ERROR_RETRY_LATER_MESSAGE, CommonBizState.UNKNOWN);
        }
    }
}
