package com.youyi.runner.config.util;

import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.conf.request.ConfigCreateRequest;
import com.youyi.domain.conf.request.ConfigDeleteRequest;
import com.youyi.domain.conf.request.ConfigQueryRequest;
import com.youyi.domain.conf.request.ConfigUpdateRequest;

import static com.youyi.common.util.param.ParamChecker.greaterThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigValidator {

    public static void validateConfigCreateRequest(ConfigCreateRequest param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .put(notBlankChecker("configValue不能为空"), param.getConfigValue())
            .validateWithThrow();
    }

    public static void validateConfigQueryRequest(ConfigQueryRequest param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getKey())
            .validateWithThrow();
    }

    public static void validateConfigUpdateRequest(ConfigUpdateRequest param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .put(notBlankChecker("newConfigValue不能为空"), param.getNewConfigValue())
            .put(greaterThanOrEqualChecker(0, "version不合法"), param.getCurrVersion())
            .validateWithThrow();
    }

    public static void validateConfigDeleteRequest(ConfigDeleteRequest param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .validateWithThrow();
    }
}
