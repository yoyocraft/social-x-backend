package com.youyi.runner.config.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.conf.helper.ConfigHelper;
import com.youyi.domain.conf.model.ConfigDO;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.config.model.ConfigInfoResponse;
import com.youyi.runner.config.model.ConfigCreateRequest;
import com.youyi.runner.config.model.ConfigDeleteRequest;
import com.youyi.runner.config.model.ConfigQueryRequest;
import com.youyi.runner.config.model.ConfigUpdateRequest;
import com.youyi.runner.config.util.ConfigValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.common.constant.PermissionConstant.CONFIG_MANAGER;
import static com.youyi.common.constant.PermissionConstant.CREATE_CONFIG;
import static com.youyi.common.constant.PermissionConstant.DELETE_CONFIG;
import static com.youyi.common.constant.PermissionConstant.READ_CONFIG;
import static com.youyi.common.constant.PermissionConstant.UPDATE_CONFIG;
import static com.youyi.runner.config.assembler.ConfigAssembler.CONFIG_ASSEMBLER;
import static com.youyi.runner.config.util.ConfigResponseUtil.createSuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.deleteSuccess;
import static com.youyi.runner.config.util.ConfigResponseUtil.listConfigSuccess;
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

    @SaCheckPermission(value = {CONFIG_MANAGER, CREATE_CONFIG}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.INSERT_CONFIG)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Boolean> createConfig(@RequestBody ConfigCreateRequest request) {
        ConfigValidator.checkConfigCreateRequest(request);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> configHelper.createConfig(configDO),
            CommonOperationUtil::tooManyRequestError,
            request.getConfigKey()
        );
        return createSuccess(request);
    }

    @SaCheckPermission(value = {CONFIG_MANAGER, READ_CONFIG}, mode = SaMode.OR)
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result<ConfigInfoResponse> queryConfig(ConfigQueryRequest request) {
        ConfigValidator.checkConfigQueryRequest(request);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(request);
        configHelper.queryConfig(configDO);
        return querySuccess(configDO, request);
    }

    @SaCheckPermission(value = {CONFIG_MANAGER, READ_CONFIG}, mode = SaMode.OR)
    @RequestMapping(value = "/cursor", method = RequestMethod.GET)
    public Result<PageCursorResult<Long, ConfigInfoResponse>> listConfig(ConfigQueryRequest request) {
        ConfigValidator.checkConfigQueryRequestForMainPage(request);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(request);
        List<ConfigDO> configDOList = configHelper.listConfig(configDO);
        return listConfigSuccess(configDOList, request);
    }

    @SaCheckPermission(value = {CONFIG_MANAGER, UPDATE_CONFIG}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.UPDATE_CONFIG)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<Boolean> updateConfig(@RequestBody ConfigUpdateRequest request) {
        ConfigValidator.checkConfigUpdateRequest(request);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> configHelper.updateConfig(configDO),
            CommonOperationUtil::tooManyRequestError,
            request.getConfigKey()
        );
        return updateSuccess(request);
    }

    @SaCheckPermission(value = {CONFIG_MANAGER, DELETE_CONFIG}, mode = SaMode.OR)
    @RecordOpLog(opType = OperationType.DELETE_CONFIG)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result<Boolean> deleteConfig(@RequestBody ConfigDeleteRequest request) {
        ConfigValidator.checkConfigDeleteRequest(request);
        ConfigDO configDO = CONFIG_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> configHelper.deleteConfig(configDO),
            CommonOperationUtil::tooManyRequestError,
            request.getConfigKey()
        );
        return deleteSuccess(request);
    }
}
