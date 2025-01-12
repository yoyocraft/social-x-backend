package com.youyi.domain.user.helper.login;

import com.youyi.common.exception.AppBizException;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.privacy.CryptoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.ReturnCode.PASSWORD_ERROR;
import static com.youyi.common.type.ReturnCode.USER_NOT_EXIST;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/12
 */
@Component
@RequiredArgsConstructor
public class EmailPasswordLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;

    @Override
    public void login(UserDO userDO) {
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
        // 4. 用户登录态
        saveUserLoginState(userDO);
    }

    void checkPassword(UserDO userDO, UserAuthPO userAuthPO) {
        // 加密密码
        String encryptedPassword = CryptoManager.encryptPassword(userDO.getCredential(), userAuthPO.getSalt());
        String systemEncryptedPassword = userAuthPO.getCredential();

        if (!encryptedPassword.equals(systemEncryptedPassword)) {
            throw AppBizException.of(PASSWORD_ERROR);
        }
    }
}
