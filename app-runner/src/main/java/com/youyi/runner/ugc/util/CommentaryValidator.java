package com.youyi.runner.ugc.util;

import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.CommentaryDeleteRequest;
import com.youyi.domain.ugc.request.CommentaryPublishRequest;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;

import static com.youyi.common.type.conf.ConfigKey.ATTACHMENT_MAX_COUNT;
import static com.youyi.common.util.param.ParamChecker.enumExistChecker;
import static com.youyi.common.util.param.ParamChecker.lessThanOrEqualChecker;
import static com.youyi.common.util.param.ParamChecker.notBlankChecker;
import static com.youyi.common.util.param.ParamChecker.snowflakeIdChecker;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
public class CommentaryValidator {

    public static void checkCommentaryPublishRequest(CommentaryPublishRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .put(notBlankChecker("评论内容不能为空"), request.getCommentary())
            .putIf(
                () -> CollectionUtils.isNotEmpty(request.getAttachmentUrls()),
                lessThanOrEqualChecker(getIntegerConfig(ATTACHMENT_MAX_COUNT), "附件数量过多"),
                Optional.ofNullable(request.getAttachmentUrls()).orElseGet(List::of).size()
            )
            .validateWithThrow();
    }

    public static void checkCommentaryQueryRequest(CommentaryQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
            .put(notBlankChecker("cursor 不能为空"), request.getCursor())
            .validateWithThrow();
    }

    public static void checkCommentaryQueryRequestForCount(CommentaryQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("UGC ID不合法"), request.getUgcId())
            .validateWithThrow();
    }

    public static void checkCommentaryDeleteRequest(CommentaryDeleteRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("评论ID不合法"), request.getCommentaryId())
            .validateWithThrow();
    }

    public static void checkUgcInteractionRequest(UgcInteractionRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(snowflakeIdChecker("Commentary ID不合法"), request.getTargetId())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .put(enumExistChecker(UgcInteractionType.class, "UGC交互类型不合法"), request.getInteractionType())
            .validateWithThrow();
    }
}
