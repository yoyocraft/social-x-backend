package com.youyi.domain.user.helper.login;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.type.user.IdentityType;
import java.util.Map;
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

    static Map<String, LoginStrategy> loginStrategyMap;

    @Override
    public void afterSingletonsInstantiated() {
        loginStrategyMap = ImmutableMap.of(
            IdentityType.EMAIL_CAPTCHA.name(), emailCaptchaLoginStrategy,
            IdentityType.EMAIL_PASSWORD.name(), emailPasswordLoginStrategy
        );
    }

    public LoginStrategy getLoginStrategy(IdentityType identityType) {
        return loginStrategyMap.get(identityType.name());
    }
}
