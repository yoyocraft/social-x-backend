package com.youyi.domain.verification.helper;

import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.verification.model.VerificationDO;
import com.youyi.infra.cache.CacheKey;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.youyi.infra.cache.key.VerificationCacheKeyRepo.ofEmailCaptchaKey;
import static com.youyi.infra.cache.key.VerificationCacheKeyRepo.ofImageCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/09
 */
@Service
@RequiredArgsConstructor
public class VerificationHelper {

    private final EmailSender emailSender;
    private final CacheRepository cacheRepository;

    public void verifyEmailCaptcha(VerificationDO verificationDO) {
        // 生成验证码
        String captcha = IdSeqUtil.genEmailCaptcha();
        verificationDO.setCaptcha(captcha);
        // 保存验证码
        saveEmailCaptcha(verificationDO);
        // 发送邮件
        emailSender.sendCaptchaEmail(verificationDO.getEmail(), captcha);
    }

    public VerificationDO verifyImageCaptcha() {
        VerificationDO verificationDO = genImageCaptcha();
        saveImageCaptcha(verificationDO);
        return verificationDO;
    }

    private void saveEmailCaptcha(VerificationDO verificationDO) {
        String cacheKey = ofEmailCaptchaKey(verificationDO.getEmail(), verificationDO.getBizType());
        cacheRepository.set(cacheKey, verificationDO.getCaptcha(), CacheKey.EMAIL_CAPTCHA.getTtl());
    }

    private void saveImageCaptcha(VerificationDO verificationDO) {
        String cacheKey = ofImageCaptchaKey(verificationDO.getCaptchaId());
        cacheRepository.set(cacheKey, verificationDO.getCaptcha(), CacheKey.EMAIL_CAPTCHA.getTtl());
    }

    private VerificationDO genImageCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 24, 5);
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        String captcha = specCaptcha.text().toLowerCase();
        String image = specCaptcha.toBase64();

        VerificationDO verificationDO = new VerificationDO();
        verificationDO.setCaptchaId(IdSeqUtil.genImageCaptchaId());
        verificationDO.setImage(image);
        verificationDO.setCaptcha(captcha);

        return verificationDO;
    }
}
