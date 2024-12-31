package com.youyi.runner.config.util;

import com.youyi.common.util.param.ParamChecker;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.core.config.param.ConfigCreateParam;
import com.youyi.core.config.param.ConfigQueryParam;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/30
 */
public class ConfigValidator {

    public static void validateConfigCreateParam(ConfigCreateParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(ParamChecker.notBlankChecker("configKey不能为空"), param.getConfigKey())
            .put(ParamChecker.notBlankChecker("configValue不能为空"), param.getConfigValue())
            .validateWithThrow();
    }

    public static void validateConfigQueryParam(ConfigQueryParam param) {
        ParamCheckerChain.newCheckerChain()
            .put(ParamChecker.notBlankChecker("configKey不能为空"), param.getKey())
            // TODO enum Checker
            // .put(ParamChecker.notBlankChecker("configValue不能为空"), param.getConfigValue())
            .validateWithThrow();
    }
}
