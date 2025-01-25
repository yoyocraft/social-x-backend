package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.domain.ugc.model.UgcCategoryInfo;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.domain.ugc.model.UgcTagInfo;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import com.youyi.domain.ugc.request.UgcTagQueryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper(
    imports = {
        UgcTagType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UgcMetadataAssembler {

    UgcMetadataAssembler UGC_METADATA_ASSEMBLER = Mappers.getMapper(UgcMetadataAssembler.class);

    UgcCategoryInfo toCategoryInfo(UgcCategoryPO po);

    @Mappings({
        @Mapping(target = "type", expression = "java(UgcTagType.of(po.getType()))")
    })
    UgcTagInfo toTagInfo(UgcTagPO po);

    @Mappings({
        @Mapping(target = "size", expression = "java(calSize(request.getSize()))")
    })
    UgcMetadataDO toDO(UgcTagQueryRequest request);

    default int calSize(int size) {
        return Math.min(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), size);
    }
}
