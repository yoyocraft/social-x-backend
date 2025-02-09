package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.request.CommentaryDeleteRequest;
import com.youyi.domain.ugc.request.CommentaryPublishRequest;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.infra.conf.util.CommonConfUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Mapper(
    imports = {
        CommentaryStatus.class,
        CommonConfUtil.class,
        UgcInteractionType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentaryAssembler {

    CommentaryAssembler COMMENTARY_ASSEMBLER = Mappers.getMapper(CommentaryAssembler.class);

    CommentaryDO toDO(CommentaryPublishRequest request);

    @Mappings({
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))")
    })
    CommentaryDO toDO(CommentaryQueryRequest request);

    CommentaryDO toDO(CommentaryDeleteRequest request);

    @Mappings({
        @Mapping(target = "commentaryId", source = "targetId"),
        @Mapping(target = "interactFlag", source = "interact"),
        @Mapping(target = "interactionType", expression = "java(UgcInteractionType.of(request.getInteractionType()))"),
    })
    CommentaryDO toDO(UgcInteractionRequest request);

}
