package com.youyi.runner.ugc.util;

import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.runner.ugc.model.UgcResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Mapper
public interface UgcConverter {

    UgcConverter UGC_CONVERTER = Mappers.getMapper(UgcConverter.class);

    @Mappings({
        @Mapping(target = "authorId", source = "author.userId"),
        @Mapping(target = "authorName", source = "author.nickName"),
        @Mapping(target = "authorAvatar", source = "author.avatar"),
        @Mapping(target = "type", expression = "java(ugcDO.getUgcType().name())"),
        @Mapping(target = "status", expression = "java(ugcDO.getStatus().name())")
    })
    UgcResponse toResponse(UgcDO ugcDO);
}
