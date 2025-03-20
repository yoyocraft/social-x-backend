package com.youyi.runner.ugc.util;

import com.youyi.domain.ugc.type.UgcInteractionType;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.UgcDeleteRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSummaryGenerateRequest;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.notNullChecker;
import static com.youyi.common.util.param.ParamChecker.snowflakeIdChecker;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;
import static com.youyi.infra.conf.core.ConfigKey.ATTACHMENT_MAX_COUNT;

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
                () -> UgcType.ARTICLE == UgcType.of(request.getUgcType()) || UgcType.QUESTION == UgcType.of(request.getUgcType()),
                notBlankChecker("标题不能为空"),
                request.getTitle()
            )
            .putIf(
                () -> !Boolean.TRUE.equals(request.getDrafting()) && UgcType.ARTICLE == UgcType.of(request.getUgcType()),
                notBlankChecker("摘要不能为空"),
                request.getSummary()
            )
            .putIf(
                () -> CollectionUtils.isNotEmpty(request.getTags()),
                lessThanOrEqualChecker(getIntegerConfig(ConfigKey.UGC_MAX_TAG_COUNT), "标签数量过多"),
                Optional.ofNullable(request.getTags()).orElseGet(List::of).size()
            )
            .putIf(
                () -> !Boolean.TRUE.equals(request.getDrafting()) && UgcType.ARTICLE == UgcType.of(request.getUgcType()) || UgcType.QUESTION == UgcType.of(request.getUgcType()),
                notBlankChecker("类别不能为空"),
                request.getCategoryId()
            )
            .putIf(
                () -> CollectionUtils.isNotEmpty(request.getAttachmentUrls()),
                lessThanOrEqualChecker(getIntegerConfig(ATTACHMENT_MAX_COUNT), "附件数量过多"),
                Optional.ofNullable(request.getAttachmentUrls()).orElseGet(List::of).size()
            )
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUgcDeleteRequest(UgcDeleteRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
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

    public static void checkUgcQueryRequestForQuerySelfCollectionUgc(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .putIfNotBlank(enumExistChecker(UgcStatus.class, "UGC状态不合法"), request.getUgcStatus())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForQueryByUgcId(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForTimelineFeed(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .putIf(
                () -> StringUtils.isNotBlank(request.getUgcType()),
                enumExistChecker(UgcType.class, "UGC类型不合法"),
                request.getUgcType()
            )
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcInteractionRequest(UgcInteractionRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getTargetId())
            .put(enumExistChecker(UgcInteractionType.class, "UGC交互类型不合法"), request.getInteractionType())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForUserPage(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("作者ID不合法"), request.getAuthorId())
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForFollowFeed(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForRecommendFeed(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForListQuestion(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("cursor不合法"), request.getCursor())
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .put(notNullChecker("问题状态不能为空"), request.getQaStatus())
            .putIfNotNull(lessThanOrEqualChecker(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), "size过大"), request.getSize())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForHotUgcList(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(enumExistChecker(UgcType.class, "UGC类型不合法"), request.getUgcType())
            .validateWithThrow();
    }

    public static void checkUgcSummaryGenerateRequest(UgcSummaryGenerateRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .validateWithThrow();
    }

    public static void checkUgcQueryRequestForUgcStatistic(UgcQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("用户ID不合法"), request.getAuthorId())
            .validateWithThrow();
    }
}
