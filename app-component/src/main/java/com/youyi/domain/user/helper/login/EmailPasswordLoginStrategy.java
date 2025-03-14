package com.youyi.domain.user.helper.login;

import com.pig4cloud.captcha.utils.CaptchaUtil;
import com.youyi.common.exception.AppBizException;
import com.youyi.domain.user.model.UserDO;
import com.youyi.domain.user.repository.UserRepository;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.privacy.CryptoManager;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.youyi.domain.user.constant.UserConstant.IMAGE_CAPTCHA_KEY;
import static com.youyi.common.type.ReturnCode.CAPTCHA_ERROR;
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
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw AppBizException.of(CAPTCHA_ERROR, "未获取到请求上下文");
        }
        HttpServletRequest request = attributes.getRequest();
        String imageCaptcha = userDO.getExtra().get(IMAGE_CAPTCHA_KEY);
        String verifyCaptcha = (String) request.getSession().getAttribute(IMAGE_CAPTCHA_KEY);
        CaptchaUtil.clear(request);
        if (!StringUtils.equalsIgnoreCase(imageCaptcha, verifyCaptcha)) {
            throw AppBizException.of(CAPTCHA_ERROR);
        }
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
