package com.youyi.runner.ugc.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.request.CommentaryDeleteRequest;
import com.youyi.domain.ugc.request.CommentaryPublishRequest;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.runner.ugc.model.CommentaryInfo;
import com.youyi.runner.ugc.model.CommentaryResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.common.constant.RepositoryConstant.TOP_COMMENTARY_ID;
import static com.youyi.runner.ugc.util.CommentaryConverter.COMMENTARY_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
public class CommentaryResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentaryResponseUtil.class);

    public static Result<Boolean> publishSuccess(CommentaryPublishRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("publish comment, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, CommentaryResponse>> queryUgcCommentarySuccess(List<CommentaryDO> commentaryDOList,
        CommentaryQueryRequest request) {
        String cursor = Optional.ofNullable(commentaryDOList.isEmpty() ? null : commentaryDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        List<CommentaryInfo> commentaryInfoList = commentaryDOList.stream().map(COMMENTARY_CONVERTER::toCommentaryInfo).toList();
        // 按照 parentId 分组
        List<CommentaryResponse> commentaryResponses = groupCommentariesByParentId(commentaryInfoList);
        Result<PageCursorResult<String, CommentaryResponse>> response = Result.success(PageCursorResult.of(commentaryResponses, cursor));
        LOGGER.info("query ugc comment, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> deleteSuccess(CommentaryDeleteRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("delete comment, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> likeSuccess(UgcInteractionRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("like comment, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    private static List<CommentaryResponse> groupCommentariesByParentId(List<CommentaryInfo> commentaryInfoList) {
        Map<String, List<CommentaryInfo>> groupedCommentaries = commentaryInfoList
            .stream()
            .collect(Collectors.groupingBy(CommentaryInfo::getParentId));

        List<CommentaryResponse> result = new ArrayList<>();

        // 获取顶层评论
        List<CommentaryInfo> rootComments = groupedCommentaries.getOrDefault(TOP_COMMENTARY_ID, Collections.emptyList());
        for (CommentaryInfo rootComment : rootComments) {
            CommentaryResponse response = new CommentaryResponse();
            response.setTopCommentary(rootComment);

            List<CommentaryInfo> replies = groupedCommentaries.getOrDefault(rootComment.getCommentaryId(), Collections.emptyList());
            response.setReplyList(replies);

            result.add(response);
        }

        return result;
    }

}
