package com.youyi.domain.user.assembler;

import com.youyi.common.type.BizType;
import com.youyi.domain.user.type.IdentityType;
import com.youyi.domain.user.type.WorkDirectionType;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.request.UserAuthenticateRequest;
import com.youyi.domain.user.request.UserEditInfoRequest;
import com.youyi.domain.user.request.UserFollowRequest;
import com.youyi.domain.user.request.UserQueryRequest;
import com.youyi.domain.user.request.UserSetPwdRequest;
import com.youyi.domain.user.request.UserVerifyCaptchaRequest;
import com.youyi.infra.conf.util.CommonConfUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Mapper(
    imports = {
        IdentityType.class,
        BizType.class,
        WorkDirectionType.class,
        CommonConfUtil.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserAssembler {

    UserAssembler USER_ASSEMBLER = Mappers.getMapper(UserAssembler.class);

    @Mappings({
        @Mapping(target = "identityType", expression = "java(IdentityType.of(request.getIdentityType()))"),
        @Mapping(target = "originalEmail", expression = "java(originalEmail(request))")
    })
    UserDO toDO(UserAuthenticateRequest request);

    @Mappings({
        @Mapping(target = "toVerifiedCaptcha", source = "captcha"),
        @Mapping(target = "originalEmail", source = "email"),
        @Mapping(target = "bizType", expression = "java(BizType.of(request.getBizType()))")
    })
    UserDO toDO(UserVerifyCaptchaRequest request);

    @Mappings({
        @Mapping(target = "bizType", expression = "java(BizType.SET_PWD)"),
        @Mapping(target = "verifyCaptchaToken", source = "token")
    })
    UserDO toDO(UserSetPwdRequest request, String token);

    @Mappings({
        @Mapping(target = "workDirection", expression = "java(WorkDirectionType.fromCode(request.getWorkDirection()))")
    })
    UserDO toDO(UserEditInfoRequest request);

    @Mappings({
        @Mapping(target = "followFlag", source = "follow")
    })
    UserDO toDO(UserFollowRequest request);

    @Mappings({
        @Mapping(target = "size", expression = "java(CommonConfUtil.calSize(request))")
    })
    UserDO toDO(UserQueryRequest request);

    default String originalEmail(UserAuthenticateRequest request) {
        IdentityType identityType = IdentityType.of(request.getIdentityType());
        if (identityType == IdentityType.EMAIL_CAPTCHA || identityType == IdentityType.EMAIL_PASSWORD) {
            return request.getIdentifier();
        }
        return StringUtils.EMPTY;
    }

}
