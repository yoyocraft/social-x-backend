package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.request.UgcDeleteRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSummaryGenerateRequest;
import com.youyi.infra.conf.util.CommonConfUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Mapper(
    imports = {
        UgcType.class,
        UgcStatus.class,
        UgcInteractionType.class,
        CommonConfUtil.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UgcAssembler {

    UgcAssembler UGC_ASSEMBLER = Mappers.getMapper(UgcAssembler.class);

    @Mappings({
        @Mapping(target = "ugcType", expression = "java(UgcType.of(request.getUgcType()))"),
        @Mapping(target = "status", expression = "java(calStatusWhenPublish(request))")
    })
    UgcDO toDO(UgcPublishRequest request);

    UgcDO toDO(UgcDeleteRequest request);

    @Mappings({
        @Mapping(target = "ugcType", expression = "java(UgcType.of(request.getUgcType()))"),
        @Mapping(target = "status", expression = "java(UgcStatus.of(request.getUgcStatus()))"),
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))"),
        @Mapping(target = "qaStatus", source = "qaStatus"),
        @Mapping(target = "editing", source = "editing")
    })
    UgcDO toDO(UgcQueryRequest request);

    @Mappings({
        @Mapping(target = "interactionType", expression = "java(UgcInteractionType.of(request.getInteractionType()))"),
        @Mapping(target = "interactFlag", source = "interact"),
        @Mapping(target = "ugcId", source = "targetId")
    })
    UgcDO toDO(UgcInteractionRequest request);

    UgcDO toDO(UgcSummaryGenerateRequest request);
    default UgcStatus calStatusWhenPublish(UgcPublishRequest request) {
        if (Boolean.TRUE.equals(request.getDrafting())) {
            return UgcStatus.DRAFT;
        }
        return UgcStatus.AUDITING;
    }
}
