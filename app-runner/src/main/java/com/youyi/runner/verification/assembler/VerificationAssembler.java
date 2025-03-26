package com.youyi.runner.verification.assembler;

import com.youyi.common.type.BizType;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.runner.verification.model.CaptchaVerifyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Mapper(
    imports = {
        BizType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VerificationAssembler {
    VerificationAssembler VERIFICATION_ASSEMBLER = Mappers.getMapper(VerificationAssembler.class);

    @Mappings({
        @Mapping(target = "bizType", expression = "java(BizType.of(request.getBizType()))")
    })
    VerificationDO toDO(CaptchaVerifyRequest request);
}
