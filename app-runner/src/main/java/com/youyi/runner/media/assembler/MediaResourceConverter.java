package com.youyi.runner.media.assembler;

import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.runner.media.model.ImageUploadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Mapper
public interface MediaResourceConverter {

    MediaResourceConverter MEDIA_RESOURCE_CONVERTER = Mappers.getMapper(MediaResourceConverter.class);

    ImageUploadResponse toImageUploadResponse(MediaResourceDO mediaResourceDO);
}
