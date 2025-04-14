package com.youyi.domain.user.helper.login;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.ReturnCode;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.privacy.CryptoManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import static com.youyi.common.type.BizType.LOGIN;
import static com.youyi.infra.cache.key.VerificationCacheKeyRepo.ofEmailCaptchaKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Component
@RequiredArgsConstructor
public class EmailCaptchaLoginStrategy implements LoginStrategy {

    private final CacheRepository cacheRepository;
    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void login(UserDO userDO) {
        // 1. 校验验证码
        checkCaptcha(userDO);

        // 2. 查询对应的用户凭证信息
        String encryptedEmail = CryptoManager.aesEncrypt(userDO.getIdentifier());
        userDO.setEncryptedEmail(encryptedEmail);
        UserAuthPO userAuthPO = userRepository.queryUserAuthByIdentityTypeAndIdentifier(userDO.getIdentityType().name(), encryptedEmail);
        // 新用户，注册
        if (Objects.isNull(userAuthPO)) {
            registerNewUser(userDO);
        }

        // 3. 查询用户信息
        UserInfoPO userInfoPO = userRepository.queryUserInfoByEmail(encryptedEmail);
        userDO.fillUserInfo(userInfoPO);

        // 4. 执行登录
        doLogin(userDO);
        // 5. 用户登录态
        saveUserLoginState(userDO);
        // 6. 删除验证码
        cleanCaptcha(userDO);
    }

    void registerNewUser(UserDO userDO) {
        userDO.create();
        UserInfoPO userInfoPO = userDO.buildToSaveUserInfoPO();
        UserAuthPO userAuthPO = userDO.buildToSaveUserAuthPO();
        transactionTemplate.executeWithoutResult(status -> {
            try {
                userRepository.insertUserInfo(userInfoPO);
                userRepository.insertUserAuth(userAuthPO);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw AppSystemException.of(ReturnCode.SYSTEM_ERROR, e);
            }
        });
    }

    private void checkCaptcha(UserDO userDO) {
        String cacheKey = ofEmailCaptchaKey(userDO.getIdentifier(), LOGIN);
        String systemCaptcha = cacheRepository.getString(cacheKey);
        if (StringUtils.isBlank(systemCaptcha)) {
            // 验证码过期
            throw AppBizException.of(ReturnCode.CAPTCHA_EXPIRED);
        }
        String userInputCaptcha = userDO.getCredential();

        if (!systemCaptcha.equals(userInputCaptcha)) {
            throw AppBizException.of(ReturnCode.CAPTCHA_ERROR);
        }
    }

    private void cleanCaptcha(UserDO userDO) {
        String cacheKey = ofEmailCaptchaKey(userDO.getIdentifier(), LOGIN);
        cacheRepository.delete(cacheKey);
    }
}
