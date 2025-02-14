package com.youyi.runner.ugc.util;

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
        Objects.class
    }
)
public interface CommentaryConverter {

    CommentaryConverter COMMENTARY_CONVERTER = Mappers.getMapper(CommentaryConverter.class);

    @Mappings({
        @Mapping(target = "commentatorId", source = "commentator.userId"),
        @Mapping(target = "commentatorNickname", source = "commentator.nickname"),
        @Mapping(target = "commentatorAvatar", source = "commentator.avatar"),
        @Mapping(target = "status", expression = "java(commentDO.getStatus().name())"),
        @Mapping(target = "adopted", expression = "java(Objects.nonNull(commentDO.getExtraData()) && Boolean.TRUE.equals(commentDO.getExtraData().getAdopted()))")
    })
    CommentaryInfo toCommentaryInfo(CommentaryDO commentDO);
}
