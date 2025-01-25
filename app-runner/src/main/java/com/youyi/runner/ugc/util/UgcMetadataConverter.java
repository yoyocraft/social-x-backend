package com.youyi.runner.ugc.util;

import com.youyi.domain.ugc.model.UgcCategoryInfo;
import com.youyi.domain.ugc.model.UgcTagInfo;
import com.youyi.runner.ugc.model.UgcCategoryInfoResponse;
import com.youyi.runner.ugc.model.UgcTagInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UgcMetadataConverter {

    UgcMetadataConverter UGC_METADATA_CONVERTER = Mappers.getMapper(UgcMetadataConverter.class);

    UgcCategoryInfoResponse toCategoryInfoResponse(UgcCategoryInfo ugcCategoryInfo);

    UgcTagInfoResponse toTagInfoResponse(UgcTagInfo ugcTagInfo);
}
