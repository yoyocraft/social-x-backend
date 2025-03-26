package com.youyi.runner.ugc.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.infra.sse.SseEmitter;
import com.youyi.runner.ugc.model.UgcDeleteRequest;
import com.youyi.runner.ugc.model.UgcInteractionRequest;
import com.youyi.runner.ugc.model.UgcPublishRequest;
import com.youyi.runner.ugc.model.UgcQueryRequest;
import com.youyi.runner.ugc.model.UgcResponse;
import com.youyi.runner.ugc.model.UgcStatisticResponse;
import com.youyi.runner.ugc.model.UgcSummaryGenerateRequest;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.infra.conf.util.CommonConfUtil.checkHasMore;
import static com.youyi.runner.ugc.assembler.UgcConverter.UGC_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(UgcResponseUtil.class);

    public static Result<Boolean> publishSuccess(UgcPublishRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("publish ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> deleteSuccess(UgcDeleteRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("delete ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> querySelfUgcSuccess(List<UgcDO> ugcPageInfo, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcPageInfo);
        logger.info("query self ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<Long, UgcResponse>> listSelfCollectedUgcSuccess(List<UgcDO> ugcPageInfo, UgcQueryRequest request) {
        Result<PageCursorResult<Long, UgcResponse>> response = buildTimeCursorResponse(ugcPageInfo);
        logger.info("query self collected ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<UgcResponse> queryUgcDetailSuccess(UgcDO ugcDO, UgcQueryRequest request) {
        Result<UgcResponse> response = Result.success(UGC_CONVERTER.toResponse(ugcDO));
        logger.info("query ugc by ugcId, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> listTimelineUgcFeedSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcDOList);
        logger.info("list timeline ugc feed, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> queryUserPageUgcSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcDOList);
        logger.info("query user page ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> listFollowUgcFeedSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcDOList);
        logger.info("list follow ugc feed, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> listRecommendUgcFeedSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcDOList);
        logger.info("list recommend ugc feed, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> listQuestionsSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        Result<PageCursorResult<String, UgcResponse>> response = buildCursorResponse(ugcDOList);
        logger.info("list questions, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<List<UgcResponse>> queryHotUgcSuccess(List<UgcDO> ugcDOList) {
        Result<List<UgcResponse>> response = Result.success(ugcDOList.stream().map(UGC_CONVERTER::toResponse).toList());
        logger.info("query hot ugc, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> interactSuccess(UgcInteractionRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("interact ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static SseEmitter generateSummarySuccess(SseEmitter sseEmitter, UgcSummaryGenerateRequest request) {
        logger.info("generate summary, request:{}", GsonUtil.toJson(request));
        return sseEmitter;
    }

    public static Result<UgcStatisticResponse> queryUgcStatisticSuccess(UgcQueryRequest request, UgcDO ugcDO) {
        Result<UgcStatisticResponse> response = Result.success(UGC_CONVERTER.toStatisticResponse(ugcDO));
        logger.info("query ugc statistic, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    private static Result<PageCursorResult<String, UgcResponse>> buildCursorResponse(List<UgcDO> ugcDOList) {
        List<UgcResponse> data = ugcDOList.stream().map(UGC_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(ugcDOList.isEmpty() ? null : ugcDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        return Result.success(PageCursorResult.of(data, cursor, checkHasMore(data)));
    }

    private static Result<PageCursorResult<Long, UgcResponse>> buildTimeCursorResponse(List<UgcDO> ugcDOList) {
        List<UgcResponse> data = ugcDOList.stream().map(UGC_CONVERTER::toResponse).toList();
        Long cursor = Optional.ofNullable(ugcDOList.isEmpty() ? null : ugcDOList.get(0).getTimeCursor()).orElse(Long.MAX_VALUE);
        return Result.success(PageCursorResult.of(data, cursor, checkHasMore(data)));
    }

}
