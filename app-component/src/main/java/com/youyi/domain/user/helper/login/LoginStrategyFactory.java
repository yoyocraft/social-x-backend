package com.youyi.domain.user.helper.login;

import com.youyi.common.type.user.IdentityType;
import java.util.EnumMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
@Component
@RequiredArgsConstructor
public class LoginStrategyFactory implements SmartInitializingSingleton {

    private final EmailCaptchaLoginStrategy emailCaptchaLoginStrategy;
    private final EmailPasswordLoginStrategy emailPasswordLoginStrategy;

    static EnumMap<IdentityType, LoginStrategy> loginStrategyMapping;

    @Override
    public void afterSingletonsInstantiated() {
        loginStrategyMapping = new EnumMap<>(IdentityType.class);
        loginStrategyMapping.put(IdentityType.EMAIL_CAPTCHA, emailCaptchaLoginStrategy);
        loginStrategyMapping.put(IdentityType.EMAIL_PASSWORD, emailPasswordLoginStrategy);
    }

    public LoginStrategy getLoginStrategy(IdentityType identityType) {
        return loginStrategyMapping.get(identityType);
    }
}
