package com.youyi.runner.user.util;

import com.youyi.common.exception.AppBizException;
import com.youyi.domain.user.type.IdentityType;
import com.youyi.infra.conf.core.Conf;
import com.youyi.infra.conf.core.ConfigKey;
import com.youyi.runner.user.model.UserAuthenticateRequest;
import com.youyi.runner.user.model.UserEditInfoRequest;
import com.youyi.runner.user.model.UserFollowRequest;
import com.youyi.runner.user.model.UserQueryRequest;
import com.youyi.runner.user.model.UserSetPwdRequest;
import com.youyi.runner.user.model.UserVerifyCaptchaRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/15
 */
class UserValidatorTest {

    @Test
    void testCheckUserAuthenticateRequest() {
        AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserAuthenticateRequest(new UserAuthenticateRequest()));
        Assertions.assertEquals("INVALID_PARAM", ex.getCode());
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserAuthenticateRequest(buildValidUserAuthenticateRequest()));
    }

    @Test
    void testCheckUserVerifyCaptchaRequest() {
        AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserVerifyCaptchaRequest(new UserVerifyCaptchaRequest()));
        Assertions.assertEquals("INVALID_PARAM", ex.getCode());
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserVerifyCaptchaRequest(buildValidUserVerifyCaptchaRequest()));
    }

    @Test
    void testCheckUserSetPwdRequestAndToken() {
        AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserSetPwdRequestAndToken(buildInvalidUserSetPwdRequest(), "token"));
        Assertions.assertEquals("INVALID_PARAM", ex.getCode());
        Assertions.assertEquals("新密码与确认密码不一致", ex.getMessage());
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserSetPwdRequestAndToken(buildValidUserSetPwdRequest(), "token"));
    }

    @Test
    void testCheckUserEditInfoRequest() {
        AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserEditInfoRequest(new UserEditInfoRequest()));
        Assertions.assertEquals("INVALID_PARAM", ex.getCode());
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserEditInfoRequest(buildValidUserEditInfoRequest()));
    }

    @Test
    void testCheckUserFollowRequest() {
        AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserFollowRequest(new UserFollowRequest()));
        Assertions.assertEquals("INVALID_PARAM", ex.getCode());
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserFollowRequest(buildValidUserFollowRequest()));
    }

    @Test
    void testCheckUserQueryRequest() {
        Assertions.assertDoesNotThrow(() -> UserValidator.checkUserQueryRequest(new UserQueryRequest()));
        try (MockedStatic<Conf> mockConf = Mockito.mockStatic(Conf.class)) {
            mockConf.when(() -> Conf.getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)).thenReturn(15);
            UserQueryRequest request = buildValidUserQueryRequest();
            // size 超过最大值
            request.setSize(100);
            AppBizException ex = Assertions.assertThrows(AppBizException.class, () -> UserValidator.checkUserQueryRequest(request));
            Assertions.assertEquals("INVALID_PARAM", ex.getCode());
            Assertions.assertEquals("size过大", ex.getMessage());

            // size 处于合理值
            request.setSize(15);
            Assertions.assertDoesNotThrow(() -> UserValidator.checkUserQueryRequest(request));
        }
    }

    private UserAuthenticateRequest buildValidUserAuthenticateRequest() {
        UserAuthenticateRequest validRequest = new UserAuthenticateRequest();
        validRequest.setIdentityType(IdentityType.EMAIL_CAPTCHA.name());
        validRequest.setIdentifier("test@test.com");
        validRequest.setCredential("123456");
        return validRequest;
    }

    private UserVerifyCaptchaRequest buildValidUserVerifyCaptchaRequest() {
        UserVerifyCaptchaRequest validRequest = new UserVerifyCaptchaRequest();
        validRequest.setEmail("test@test.com");
        validRequest.setCaptcha("123456");
        validRequest.setBizType("LOGIN");
        return validRequest;
    }

    private UserSetPwdRequest buildInvalidUserSetPwdRequest() {
        UserSetPwdRequest invalidRequest = new UserSetPwdRequest();
        invalidRequest.setNewPassword("newPassword");
        invalidRequest.setConfirmPassword("confirmPassword");
        return invalidRequest;
    }

    private UserSetPwdRequest buildValidUserSetPwdRequest() {
        UserSetPwdRequest validRequest = new UserSetPwdRequest();
        validRequest.setNewPassword("newPassword");
        validRequest.setConfirmPassword("newPassword");
        return validRequest;
    }

    private UserEditInfoRequest buildValidUserEditInfoRequest() {
        UserEditInfoRequest validRequest = new UserEditInfoRequest();
        validRequest.setUserId("1888241794295930880");
        validRequest.setNickname("nickname");
        validRequest.setWorkStartTime("2025-01");
        validRequest.setWorkDirection(0);
        validRequest.setBio("bio");
        validRequest.setJobTitle("job title");
        validRequest.setCompany("company");
        validRequest.setAvatar("avatar");
        return validRequest;
    }

    private UserFollowRequest buildValidUserFollowRequest() {
        UserFollowRequest validRequest = new UserFollowRequest();
        validRequest.setReqId("1888241794295930880");
        validRequest.setFollowUserId("1888241794295930880");
        return validRequest;
    }

    private UserQueryRequest buildValidUserQueryRequest() {
        UserQueryRequest validRequest = new UserQueryRequest();
        validRequest.setUserId("1888241794295930880");
        validRequest.setCursor(0L);
        return validRequest;
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme