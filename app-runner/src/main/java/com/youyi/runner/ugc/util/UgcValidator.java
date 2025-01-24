package com.youyi.runner.ugc.util;

import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSetStatusRequest;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcValidator {

    public static void checkUgcPublishRequest(UgcPublishRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .put(notBlankChecker("内容不能为空"), request.getContent())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForQuerySelfUgc(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putIfNotBlank(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .putIfNotBlank(enumExistChecker(UgcStatusType.class, "UGC状态不合法"), request.getUgcStatus())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForQueryByUgcId(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("UGC ID不能为空"), request.getUgcId())
            .validateWithThrow();
    }

    public static void checkUgcSetStatusRequest(UgcSetStatusRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("UGC ID不能为空"), request.getUgcId())
            .put(enumExistChecker(UgcStatusType.class, "UGC状态不合法"), request.getStatus())
            .validateWithThrow();
    }
}
