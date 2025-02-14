package com.youyi.runner.ugc.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.request.UgcDeleteRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSetStatusRequest;
import com.youyi.runner.ugc.model.UgcResponse;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.ugc.util.UgcConverter.UGC_CONVERTER;

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
        List<UgcResponse> data = ugcPageInfo.stream().map(UGC_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(ugcPageInfo.isEmpty() ? null : ugcPageInfo.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        Result<PageCursorResult<String, UgcResponse>> response = Result.success(PageCursorResult.of(data, cursor));
        logger.info("query self ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<UgcResponse> queryByUgcIdSuccess(UgcDO ugcDO, UgcQueryRequest request) {
        Result<UgcResponse> response = Result.success(UGC_CONVERTER.toResponse(ugcDO));
        logger.info("query ugc by ugcId, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> setStatusSuccess(UgcSetStatusRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("set ugc status, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> queryByCursorForMainPageSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        List<UgcResponse> data = ugcDOList.stream().map(UGC_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(ugcDOList.isEmpty() ? null : ugcDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        Result<PageCursorResult<String, UgcResponse>> response = Result.success(PageCursorResult.of(data, cursor));
        logger.info("query ugc article by cursor, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageCursorResult<String, UgcResponse>> queryByCursorForFollowPageSuccess(List<UgcDO> ugcDOList, UgcQueryRequest request) {
        List<UgcResponse> data = ugcDOList.stream().map(UGC_CONVERTER::toResponse).toList();
        String cursor = Optional.ofNullable(ugcDOList.isEmpty() ? null : ugcDOList.get(0).getCursor()).orElse(SymbolConstant.EMPTY);
        Result<PageCursorResult<String, UgcResponse>> response = Result.success(PageCursorResult.of(data, cursor));
        logger.info("query ugc article by cursor, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<Boolean> interactSuccess(UgcInteractionRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        logger.info("interact ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

}
