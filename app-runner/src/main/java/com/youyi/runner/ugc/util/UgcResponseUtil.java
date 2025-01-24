package com.youyi.runner.ugc.util;

import com.youyi.common.base.PageResult;
import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.runner.ugc.model.UgcResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import static com.youyi.runner.ugc.util.UgcConverter.UGC_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
public class UgcResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcResponseUtil.class);

    public static Result<Boolean> publishSuccess(UgcPublishRequest request) {
        Result<Boolean> response = Result.success(Boolean.TRUE);
        LOGGER.info("publish ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }

    public static Result<PageResult<UgcResponse>> querySelfUgcSuccess(Page<UgcDO> ugcPageInfo, UgcQueryRequest request) {
        long total = ugcPageInfo.getTotalElements();
        long size = ugcPageInfo.getPageable().getPageSize();
        long page = ugcPageInfo.getPageable().getPageNumber();
        List<UgcResponse> data = ugcPageInfo.getContent().stream().map(UGC_CONVERTER::toResponse).toList();

        Result<PageResult<UgcResponse>> response = Result.success(PageResult.of(total, page, size, data));
        LOGGER.info("query self ugc, request:{}, response:{}", GsonUtil.toJson(request), GsonUtil.toJson(response));
        return response;
    }
}
