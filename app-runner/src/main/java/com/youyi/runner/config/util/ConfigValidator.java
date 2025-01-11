package com.youyi.runner.config.util;

import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.conf.param.ConfigCreateParam;
import com.youyi.domain.conf.param.ConfigDeleteParam;
import com.youyi.domain.conf.param.ConfigQueryParam;
import com.youyi.domain.conf.param.ConfigUpdateParam;

import static com.youyi.common.util.param.ParamChecker.greaterThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigValidator {

    public static void validateConfigCreateParam(ConfigCreateParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .put(notBlankChecker("configValue不能为空"), param.getConfigValue())
            .validateWithThrow();
    }

    public static void validateConfigQueryParam(ConfigQueryParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getKey())
            .validateWithThrow();
    }

    public static void validateConfigUpdateParam(ConfigUpdateParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .put(notBlankChecker("newConfigValue不能为空"), param.getNewConfigValue())
            .put(greaterThanOrEqualChecker(0, "version不合法"), param.getCurrVersion())
            .validateWithThrow();
    }

    public static void validateConfigDeleteParam(ConfigDeleteParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("configKey不能为空"), param.getConfigKey())
            .validateWithThrow();
    }
}
