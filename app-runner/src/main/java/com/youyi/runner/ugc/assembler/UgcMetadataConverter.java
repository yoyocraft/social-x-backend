package com.youyi.runner.ugc.assembler;

import com.youyi.domain.ugc.model.UgcCategoryInfo;
import com.youyi.domain.ugc.model.UgcTagInfo;
import com.youyi.runner.ugc.model.UgcCategoryInfoResponse;
import com.youyi.runner.ugc.model.UgcTagInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper
public interface UgcMetadataConverter {

    UgcMetadataConverter UGC_METADATA_CONVERTER = Mappers.getMapper(UgcMetadataConverter.class);

    @Mappings({
        @Mapping(target = "icon", expression = "java(ugcCategoryInfo.getExtraData().getIcon())"),
        @Mapping(target = "qaTemplate", expression = "java(ugcCategoryInfo.getExtraData().getQaTemplate())"),
        @Mapping(target = "qaSuggestion", expression = "java(ugcCategoryInfo.getExtraData().getQaSuggestion())")
    })
    UgcCategoryInfoResponse toCategoryInfoResponse(UgcCategoryInfo ugcCategoryInfo);

    UgcTagInfoResponse toTagInfoResponse(UgcTagInfo ugcTagInfo);
}
