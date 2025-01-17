package com.youyi.runner.media.util;

import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.runner.media.model.ImageUploadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Mapper
public interface MediaResourceConverter {

    MediaResourceConverter MEDIA_RESOURCE_CONVERTER = Mappers.getMapper(MediaResourceConverter.class);

    @Mappings({
        @Mapping(target = "url", source = "accessUrl")
    })
    ImageUploadResponse toImageUploadResponse(MediaResourceDO mediaResourceDO);
}
