package com.youyi.domain.user.assembler;

import com.youyi.common.type.IdentityType;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.param.UserAuthenticateParam;
import com.youyi.domain.user.param.UserVerifyCaptchaParam;
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
        NotificationType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserAssembler {

    UserAssembler USER_ASSEMBLER = Mappers.getMapper(UserAssembler.class);

    @Mappings({
        @Mapping(target = "identityType", expression = "java(IdentityType.of(param.getIdentityType()))"),
        @Mapping(target = "originalEmail", expression = "java(originalEmail(param))")
    })
    UserDO toDO(UserAuthenticateParam param);

    @Mappings({
        @Mapping(target = "toVerifiedCaptcha", source = "captcha"),
        @Mapping(target = "originalEmail", source = "email"),
        @Mapping(target = "notificationType", expression = "java(NotificationType.of(param.getBizType()))")
    })
    UserDO toDO(UserVerifyCaptchaParam param);

    default String originalEmail(UserAuthenticateParam param) {
        IdentityType identityType = IdentityType.of(param.getIdentityType());
        if (identityType == IdentityType.EMAIL_CAPTCHA || identityType == IdentityType.EMAIL_PASSWORD) {
            return param.getIdentifier();
        }
        return StringUtils.EMPTY;
    }
}
