package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.domain.ugc.request.UgcTagQueryRequest;
import com.youyi.infra.conf.util.CommonConfUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper(
    imports = {
        UgcTagType.class,
        CommonConfUtil.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UgcMetadataAssembler {

    UgcMetadataAssembler UGC_METADATA_ASSEMBLER = Mappers.getMapper(UgcMetadataAssembler.class);

    @Mappings({
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))")
    })
    UgcMetadataDO toDO(UgcTagQueryRequest request);
}
