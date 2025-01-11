package com.youyi.runner.user.util;

import com.youyi.domain.user.model.UserDO;
import com.youyi.runner.user.model.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mappings({
        @Mapping(target = "gender", expression = "java(userDO.getGender().name())"),
        @Mapping(target = "role", expression = "java(userDO.getRole().name())"),
        @Mapping(target = "desensitizedEmail", source = "originalEmail"),
        @Mapping(target = "desensitizedMobile", source = "originalPhone")
    })
    UserVO toVO(UserDO userDO);
}
