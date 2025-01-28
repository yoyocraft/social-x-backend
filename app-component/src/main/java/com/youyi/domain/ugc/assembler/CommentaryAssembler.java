package com.youyi.domain.ugc.assembler;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.request.CommentaryDeleteRequest;
import com.youyi.domain.ugc.request.CommentaryPublishRequest;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static com.youyi.infra.conf.core.SystemConfigService.getLongConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Mapper(
    imports = {
        CommentaryStatus.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentaryAssembler {

    CommentaryAssembler COMMENTARY_ASSEMBLER = Mappers.getMapper(CommentaryAssembler.class);

    CommentaryDO toDO(CommentaryPublishRequest request);

    @Mappings({
        @Mapping(target = "size", expression = "java(calSize(request))")
    })
    CommentaryDO toDO(CommentaryQueryRequest request);

    CommentaryDO toDO(CommentaryDeleteRequest request);

    default int calSize(CommentaryQueryRequest request) {
        if (Objects.isNull(request.getSize()) || request.getSize() <= 0) {
            return Math.toIntExact(getLongConfig(ConfigKey.DEFAULT_PAGE_SIZE));
        }
        return request.getSize();
    }
}
