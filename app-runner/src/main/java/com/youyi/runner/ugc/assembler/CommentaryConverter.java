package com.youyi.runner.ugc.assembler;

import com.youyi.domain.ugc.type.CommentaryStatus;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.runner.ugc.model.CommentaryInfo;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Mapper(
    imports = {
        Objects.class,
        CommentaryStatus.class
    }
)
public interface CommentaryConverter {

    CommentaryConverter COMMENTARY_CONVERTER = Mappers.getMapper(CommentaryConverter.class);

    @Mappings({
        @Mapping(target = "commentatorId", source = "commentator.userId"),
        @Mapping(target = "commentatorNickname", source = "commentator.nickname"),
        @Mapping(target = "commentatorAvatar", source = "commentator.avatar"),
        @Mapping(target = "status", expression = "java(commentDO.getStatus().name())"),
        @Mapping(target = "adopted", expression = "java(Objects.nonNull(commentDO.getExtraData()) && Boolean.TRUE.equals(commentDO.getExtraData().getAdopted()))"),
        @Mapping(target = "featured", expression = "java(Objects.nonNull(commentDO.getExtraData()) && Boolean.TRUE.equals(commentDO.getExtraData().getFeatured()))"),
        @Mapping(target = "sensitive", expression = "java(CommentaryStatus.SENSITIVE == commentDO.getStatus())"),
    })
    CommentaryInfo toCommentaryInfo(CommentaryDO commentDO);
}
