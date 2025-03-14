package com.youyi.runner.verification.util;

import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.runner.verification.model.ImageCaptchaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/14
 */
@Mapper
public interface VerificationConverter {

    VerificationConverter VERIFICATION_CONVERTER = Mappers.getMapper(VerificationConverter.class);

    ImageCaptchaResponse toResponse(VerificationDO verificationDO);
}
