package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.domain.ugc.request.UgcTagQueryRequest;
import java.util.Objects;
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

    @Mappings({
        @Mapping(target = "size", expression = "java(calSize(request))")
    })
    UgcMetadataDO toDO(UgcTagQueryRequest request);

    default int calSize(UgcTagQueryRequest request) {
        if (Objects.isNull(request.getSize()) || request.getSize() <= 0) {
            return getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE);
        }
        return Math.min(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), request.getSize());
    }
}
