package com.youyi.domain.verification.helper;

import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.infra.cache.manager.CacheManager;
import com.youyi.infra.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.youyi.infra.cache.repo.VerificationCacheRepo.EMAIL_CAPTCHA_TTL;
import static com.youyi.infra.cache.repo.VerificationCacheRepo.ofEmailCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Service
@RequiredArgsConstructor
public class VerificationHelper {

    private final EmailSender emailSender;
    private final CacheManager cacheManager;

    public void verifyEmailCaptcha(VerificationDO verificationDO) {
        // 生成验证码
        String captcha = IdSeqUtil.genEmailCaptcha();
        verificationDO.setCaptcha(captcha);
        // 保存验证码
        saveCaptcha(verificationDO);
        // 发送邮件
        // TODO youyi 2025/1/9 支持重试，防抖，异步发送即可
        emailSender.sendCaptchaEmail(verificationDO.getEmail(), captcha);
    }

    private void saveCaptcha(VerificationDO verificationDO) {
        String cacheKey = ofEmailCaptchaKey(verificationDO.getEmail(), verificationDO.getBizType());
        cacheManager.set(cacheKey, verificationDO.getCaptcha(), EMAIL_CAPTCHA_TTL);
    }
}
