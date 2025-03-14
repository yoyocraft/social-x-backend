package com.youyi.domain.verification.model;

import com.youyi.common.type.BizType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Getter
@Setter
public class VerificationDO {

    private String email;
    private String captcha;
    private BizType bizType;

    private String captchaId;
    private String image;
}
