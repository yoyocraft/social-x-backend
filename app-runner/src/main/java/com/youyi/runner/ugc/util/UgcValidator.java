package com.youyi.runner.ugc.util;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.UgcDeleteRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSetStatusRequest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.snowflakeIdChecker;
import static com.youyi.common.util.param.ParamChecker.trueChecker;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcValidator {

    public static void checkUgcPublishRequest(UgcPublishRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .put(notBlankChecker("内容不能为空"), request.getContent())
            .putIf(
                () -> UgcType.ARTICLE == UgcType.of(request.getUgcType()),
                notBlankChecker("标题不能为空"),
                request.getTitle()
            )
            .putIf(
                () -> UgcType.ARTICLE == UgcType.of(request.getUgcType()),
                notBlankChecker("摘要不能为空"),
                request.getSummary()
            )
            .putIf(
                () -> CollectionUtils.isNotEmpty(request.getTags()),
                lessThanOrEqualChecker(getIntegerConfig(ConfigKey.UGC_MAX_TAG_COUNT), "标签数量过多"),
                request.getTags().size()
            )
            .putBatchIf(
                () -> Boolean.TRUE.equals(request.getDrafting()),
                notBlankChecker("类别不能为空"),
                List.of(request.getCategoryId(), request.getCategoryName())
            )
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUgcDeleteRequest(UgcDeleteRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不能为空"), request.getUgcId())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForQuerySelfUgc(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putIfNotBlank(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .putIfNotBlank(enumExistChecker(UgcStatus.class, "UGC状态不合法"), request.getUgcStatus())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForQueryByUgcId(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不能为空"), request.getUgcId())
            .validateWithThrow();
    }

    public static void checkUgcSetStatusRequest(UgcSetStatusRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不能为空"), request.getUgcId())
            .put(enumExistChecker(UgcStatus.class, "UGC状态不合法"), request.getStatus())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForMainPage(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcInteractionRequest(UgcInteractionRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不能为空"), request.getTargetId())
            .put(enumExistChecker(UgcInteractionType.class, "UGC交互类型不合法"), request.getInteractionType())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForUserPage(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("作者ID不能为空"), request.getAuthorId())
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForFollowPage(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(trueChecker("followFeed不能为空"), request.getFollowFeed())
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }
}
