package com.youyi.domain.user.helper.login;

import com.youyi.common.exception.AppBizException;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.privacy.CryptoManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.ReturnCode.CAPTCHA_ERROR;
import static com.youyi.common.type.ReturnCode.CAPTCHA_EXPIRED;
import static com.youyi.common.type.ReturnCode.PASSWORD_ERROR;
import static com.youyi.common.type.ReturnCode.USER_NOT_EXIST;
import static com.youyi.domain.user.constant.UserConstant.IMAGE_CAPTCHA_ID;
import static com.youyi.domain.user.constant.UserConstant.IMAGE_CAPTCHA_KEY;
import static com.youyi.infra.cache.key.VerificationCacheKeyRepo.ofImageCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Component
@RequiredArgsConstructor
public class EmailPasswordLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;
    private final CacheRepository cacheRepository;

    @Override
    public void login(UserDO userDO) {
        // 0. 校验验证码
        checkImageCaptcha(userDO);
        // 1. 查询对应的用户凭证信息
        String encryptedEmail = CryptoManager.aesEncrypt(userDO.getIdentifier());
        userDO.setEncryptedEmail(encryptedEmail);
        UserAuthPO userAuthPO = userRepository.queryUserAuthByIdentityTypeAndIdentifier(userDO.getIdentityType().name(), encryptedEmail);
        if (userAuthPO == null) {
            throw AppBizException.of(USER_NOT_EXIST);
        }
        // 2. 校验密码
        checkPassword(userDO, userAuthPO);
        // 3. 查询用户信息
        UserInfoPO userInfoPO = userRepository.queryUserInfoByEmail(encryptedEmail);
        userDO.fillUserInfo(userInfoPO);
        // 4. 执行登录
        doLogin(userDO);
        // 5. 用户登录态
        saveUserLoginState(userDO);
    }

    private void checkImageCaptcha(UserDO userDO) {
        String captchaId = userDO.getExtra().get(IMAGE_CAPTCHA_ID);
        String cacheKey = ofImageCaptchaKey(captchaId);
        String verifyCaptcha = cacheRepository.getString(cacheKey);
        if (StringUtils.isBlank(verifyCaptcha)) {
            throw AppBizException.of(CAPTCHA_EXPIRED);
        }
        String imageCaptcha = userDO.getExtra().get(IMAGE_CAPTCHA_KEY);
        if (!StringUtils.equalsIgnoreCase(imageCaptcha, verifyCaptcha)) {
            throw AppBizException.of(CAPTCHA_ERROR);
        }
        // 验证码校验通过，删除验证码
        cacheRepository.delete(cacheKey);
    }

    private void checkPassword(UserDO userDO, UserAuthPO userAuthPO) {
        // 加密密码
        String encryptedPassword = CryptoManager.encryptPassword(userDO.getCredential(), userAuthPO.getSalt());
        String systemEncryptedPassword = userAuthPO.getCredential();

        if (!encryptedPassword.equals(systemEncryptedPassword)) {
            throw AppBizException.of(PASSWORD_ERROR);
        }
    }

}
