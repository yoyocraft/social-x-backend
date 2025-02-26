package com.youyi.runner.ugc.util;

import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.user.model.UserDO;
import com.youyi.runner.ugc.model.UgcResponse;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import java.util.Objects;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Mapper(
    imports = {
        Objects.class,
        UgcType.class,
        Optional.class,
        UgcStatus.class
    }
)
public interface UgcConverter {

    UgcConverter UGC_CONVERTER = Mappers.getMapper(UgcConverter.class);

    @Mappings({
        @Mapping(target = "author", expression = "java(toAuthorResponse(ugcDO.getAuthor()))"),
        @Mapping(target = "type", expression = "java(Optional.ofNullable(ugcDO.getUgcType()).orElse(UgcType.ARTICLE).name())"),
        @Mapping(target = "status", expression = "java(Optional.ofNullable(ugcDO.getStatus()).orElse(UgcStatus.PUBLISHED).name())"),
        @Mapping(target = "hasSolved", expression = "java(Objects.nonNull(ugcDO.getExtraData()) && Boolean.TRUE.equals(ugcDO.getExtraData().getHasSolved()))")
    })
    UgcResponse toResponse(UgcDO ugcDO);

    default UserBasicInfoResponse toAuthorResponse(UserDO userDO) {
        UserBasicInfoResponse author = new UserBasicInfoResponse();
        if (Objects.isNull(userDO)) {
            return author;
        }
        author.setUserId(userDO.getUserId());
        author.setNickname(userDO.getNickname());
        author.setAvatar(userDO.getAvatar());
        author.setBio(userDO.getBio());
        author.setHasFollowed(userDO.getHasFollowed());
        return author;
    }
}
