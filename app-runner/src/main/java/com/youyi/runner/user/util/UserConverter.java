package com.youyi.runner.user.util;

import com.youyi.common.util.CommonOperationUtil;
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
@Mapper(
    imports = {
        CommonOperationUtil.class
    }
)
public interface UserConverter {

    UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mappings({
        @Mapping(target = "role", expression = "java(userDO.getRole().name())"),
        @Mapping(target = "desensitizedEmail", source = "originalEmail"),
        @Mapping(target = "desensitizedMobile", source = "originalPhone"),
        @Mapping(target = "workDirection", expression = "java(userDO.getWorkDirection().getCode())"),
        @Mapping(target = "joinTime", expression = "java(CommonOperationUtil.date2Timestamp(userDO.getJoinTime()))")
    })
    UserBasicInfoResponse toResponse(UserDO userDO);

    @Mappings({
        @Mapping(target = "token", source = "verifyCaptchaToken")
    })
    VerifyCaptchaResponse toVerifyCaptchaResponse(UserDO userDO);

}
