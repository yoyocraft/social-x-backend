package com.youyi.runner.ugc.util;

import com.youyi.common.util.param.ParamCheckerChain;
import com.youyi.domain.ugc.request.CommentaryDeleteRequest;
import com.youyi.domain.ugc.request.CommentaryPublishRequest;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;

import static com.youyi.common.util.param.ParamChecker.notBlankChecker;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
public class CommentaryValidator {

    public static void checkCommentaryPublishRequest(CommentaryPublishRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("UGC ID不能为空"), request.getUgcId())
            .put(notBlankChecker("请求ID不能为空"), request.getReqId())
            .put(notBlankChecker("评论内容不能为空"), request.getCommentary())
            .validateWithThrow();
    }

    public static void checkCommentaryQueryRequest(CommentaryQueryRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("UGC ID不能为空"), request.getUgcId())
            .put(notBlankChecker("cursor 不能为空"), request.getCursor())
            .validateWithThrow();
    }

    public static void checkCommentaryDeleteRequest(CommentaryDeleteRequest request) {
        ParamCheckerChain.newCheckerChain()
            .put(notBlankChecker("评论ID不能为空"), request.getCommentaryId())
            .validateWithThrow();
    }
}
