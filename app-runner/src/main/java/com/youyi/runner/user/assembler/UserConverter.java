package com.youyi.runner.user.assembler;

import com.youyi.domain.user.model.UserDO;
import com.youyi.runner.user.model.UserBasicInfoResponse;
import com.youyi.runner.user.model.VerifyCaptchaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Mapper
public interface UserConverter {

    UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mappings({
        @Mapping(target = "role", expression = "java(userDO.getRole().name())"),
        @Mapping(target = "desensitizedEmail", source = "originalEmail"),
        @Mapping(target = "desensitizedMobile", source = "originalPhone"),
        @Mapping(target = "workDirection", expression = "java(userDO.getWorkDirection().getCode())"),
        @Mapping(target = "hasFollowed", source = "hasFollowed")
    })
    UserBasicInfoResponse toResponse(UserDO userDO);

    @Mappings({
        @Mapping(target = "token", source = "verifyCaptchaToken")
    })
    VerifyCaptchaResponse toVerifyCaptchaResponse(UserDO userDO);

}
