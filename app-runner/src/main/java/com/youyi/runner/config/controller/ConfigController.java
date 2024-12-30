package com.youyi.runner.config.controller;

import com.youyi.common.base.Result;
import com.youyi.common.constant.CommonBizState;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.ServerType;
import com.youyi.common.util.GsonUtil;
import com.youyi.core.config.assembler.ConfigAssembler;
import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.helper.ConfigHelper;
import com.youyi.core.config.param.ConfigCreateParam;
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
import static com.youyi.runner.config.util.ConfigResponseUtil.createFail;
import static com.youyi.runner.config.util.ConfigResponseUtil.createSuccess;

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
            ConfigDO configDO = ConfigAssembler.INSTANCE.toDO(param);
            configHelper.createConfig(configDO);
            return createSuccess(param);
        } catch (AppBizException e) {
            return createFail(param, e.getCode(), e.getMessage(), CommonBizState.FAILED);
        } catch (Exception e) {
            serverExpLog(LOGGER, ServerType.HTTP, "createConfig", GsonUtil.toJson(param), e);
            return createFail(param, SYSTEM_ERROR_RETRY_LATER, SYSTEM_ERROR_RETRY_LATER_MESSAGE, CommonBizState.UNKNOWN);
        }
    }
}
