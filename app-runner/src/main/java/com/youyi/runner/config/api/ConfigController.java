package com.youyi.runner.config.api;

import com.youyi.common.anno.RecordOpLog;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.domain.conf.helper.ConfigHelper;
import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.domain.conf.param.ConfigCreateParam;
import com.youyi.domain.conf.param.ConfigDeleteParam;
import com.youyi.domain.conf.param.ConfigQueryParam;
import com.youyi.domain.conf.param.ConfigUpdateParam;
import com.youyi.runner.config.model.ConfigVO;
import com.youyi.runner.config.util.ConfigValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.conf.assembler.ConfigAssembler.CONFIG_ASSEMBLER;
import static com.youyi.runner.config.util.ConfigResponseUtil.createSuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.deleteSuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.querySuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.updateSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigHelper configHelper;

    @RecordOpLog(opType = OperationType.INSERT_CONFIG, system = true)
    @RequestMapping(method = RequestMethod.POST)
    public Result<Boolean> createConfig(@RequestBody ConfigCreateParam param) {
        ConfigValidator.validateConfigCreateParam(param);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
        configHelper.createConfig(configDO);
        return createSuccess(param);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result<ConfigVO> queryConfig(ConfigQueryParam param) {
        ConfigValidator.validateConfigQueryParam(param);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
        ConfigDO config = configHelper.queryConfig(configDO);
        return querySuccess(config, param);
    }

    @RecordOpLog(opType = OperationType.UPDATE_CONFIG, system = true)
    @RequestMapping(method = RequestMethod.PUT)
    public Result<Boolean> updateConfig(@RequestBody ConfigUpdateParam param) {
        ConfigValidator.validateConfigUpdateParam(param);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
        configHelper.updateConfig(configDO);
        return updateSuccess(param);
    }

    @RecordOpLog(opType = OperationType.DELETE_CONFIG, system = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public Result<Boolean> deleteConfig(@RequestBody ConfigDeleteParam param) {
        ConfigValidator.validateConfigDeleteParam(param);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(param);
        configHelper.deleteConfig(configDO);
        return deleteSuccess(param);
    }
}
