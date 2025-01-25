package com.youyi.runner.ugc.util;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.UgcTagQueryRequest;

import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notNullAndPositiveChecker;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
public class UgcMetadataValidator {

    public static void checkUgcTagQueryRequest(UgcTagQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notNullAndPositiveChecker("cursor不合法"), request.getCursor())
            .put(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }
}
