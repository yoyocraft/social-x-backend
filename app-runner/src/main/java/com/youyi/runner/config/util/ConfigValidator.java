package com.youyi.runner.config.util;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.conf.ConfigType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.conf.request.ConfigCreateRequest;
import com.youyi.domain.conf.request.ConfigDeleteRequest;
import com.youyi.domain.conf.request.ConfigQueryRequest;
import com.youyi.domain.conf.request.ConfigUpdateRequest;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.greaterThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigValidator {

    public static void validateConfigCreateRequest(ConfigCreateRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), request.getConfigKey())
            .put(notBlankChecker("configValue不能为空"), request.getConfigValue())
            .put(enumExistChecker(ConfigType.class, "configType不合法"), request.getConfigType())
            .validateWithThrow();
    }

    public static void validateConfigQueryRequest(ConfigQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), request.getKey())
            .validateWithThrow();
    }

    public static void validateConfigQueryRequestForMainPage(ConfigQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(greaterThanOrEqualChecker(0L, "cursor不能为空"), request.getCursor())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void validateConfigUpdateRequest(ConfigUpdateRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), request.getConfigKey())
            .put(notBlankChecker("newConfigValue不能为空"), request.getNewConfigValue())
            .put(greaterThanOrEqualChecker(0, "version不合法"), request.getCurrVersion())
            .validateWithThrow();
    }

    public static void validateConfigDeleteRequest(ConfigDeleteRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), request.getConfigKey())
            .validateWithThrow();
    }
}
