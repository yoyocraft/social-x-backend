package com.youyi.runner.ugc.util;

import com.youyi.common.base.Result;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.runner.ugc.model.UgcCategoryInfoResponse;
import com.youyi.runner.ugc.model.UgcMetadataResponse;
import com.youyi.runner.ugc.model.UgcTagInfoResponse;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.runner.ugc.util.UgcMetadataConverter.UGC_METADATA_CONVERTER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
public class UgcMetadataResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(UgcMetadataResponseUtil.class);

    public static Result<UgcMetadataResponse> queryUgcCategorySuccess(UgcMetadataDO ugcMetadataDO) {
        List<UgcCategoryInfoResponse> ugcCategoryInfoResponseList = ugcMetadataDO.getUgcCategoryList()
            .stream()
            .map(UGC_METADATA_CONVERTER::toCategoryInfoResponse)
            .sorted(Comparator.comparingInt(UgcCategoryInfoResponse::getPriority))
            .toList();
        Result<UgcMetadataResponse> response = Result.success(UgcMetadataResponse.of(ugcCategoryInfoResponseList, List.of()));
        logger.info("query ugc category, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<UgcMetadataResponse> queryUgcInterestTagSuccess(UgcMetadataDO ugcMetadataDO) {
        List<UgcTagInfoResponse> ugcTagInfoResponseList = ugcMetadataDO.getUgcTagList()
            .stream()
            .map(UGC_METADATA_CONVERTER::toTagInfoResponse)
            .sorted(Comparator.comparingInt(UgcTagInfoResponse::getPriority))
            .toList();
        Result<UgcMetadataResponse> response = Result.success(UgcMetadataResponse.of(List.of(), ugcTagInfoResponseList));
        logger.info("query ugc interest tag, response:{}", GsonUtil.toJson(response));
        return response;
    }

    public static Result<UgcMetadataResponse> queryUgcArticleTagSuccess(UgcMetadataDO ugcMetadataDO) {
        List<UgcTagInfoResponse> ugcTagInfoResponseList = ugcMetadataDO.getUgcTagList()
            .stream()
            .map(UGC_METADATA_CONVERTER::toTagInfoResponse)
            .sorted(Comparator.comparingInt(UgcTagInfoResponse::getPriority))
            .toList();

        Result<UgcMetadataResponse> response = Result.success(UgcMetadataResponse.of(List.of(), ugcTagInfoResponseList));
        logger.info("query ugc article tag with cursor, response:{}", GsonUtil.toJson(response));
        return response;
    }
}
