package com.youyi.domain.user.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.util.crypto.IvGenerator;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.domain.user.type.IdentityType;
import com.youyi.infra.privacy.CryptoManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import static com.youyi.infra.conf.core.Conf.getStringConfig;
import static com.youyi.infra.conf.core.ConfigKey.DEFAULT_AVATAR;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/11
 */
class UserRepositoryIntegrationTest extends BaseIntegrationTest {

    private static final String EMAIL_FORMAT = "test%s@socialx.com";

    // need js-md5 encrypt password
    private static final String INIT_MD5_PASSWORD = "SocialX";

    private static final Integer USER_COUNT = 20000;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Test
    void testInsertUser() {
        List<UserInfoPO> userInfoPOList = new ArrayList<>(USER_COUNT);
        List<UserAuthPO> userAuthPOList = new ArrayList<>(USER_COUNT);
        for (int i = 1; i <= USER_COUNT; i++) {
            String userId = IdSeqUtil.genUserId();
            String email = buildEmail(i);
            // 构造用户基本信息
            userInfoPOList.add(buildUserInfo(userId, email));
            // 构造用户凭证信息
            userAuthPOList.add(buildUserAuth(userId, email));
        }
        transactionTemplate.executeWithoutResult(status -> {
            try {
                userRepository.insertUserInfoBatch(userInfoPOList);
                userRepository.insertUserAuthBatch(userAuthPOList);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw AppSystemException.of(ReturnCode.SYSTEM_ERROR, e);
            }
        });
    }

    UserInfoPO buildUserInfo(String userId, String email) {
        UserInfoPO userInfoPO = new UserInfoPO();
        userInfoPO.setUserId(userId);
        userInfoPO.setEmail(CryptoManager.aesEncrypt(email));
        userInfoPO.setEmailIv(IvGenerator.generateIv(email));
        userInfoPO.setNickname(IdSeqUtil.genUserNickname());
        userInfoPO.setPhone(EMPTY);
        userInfoPO.setPhoneIv(EMPTY);
        userInfoPO.setAvatar(getStringConfig(DEFAULT_AVATAR));
        return userInfoPO;
    }

    UserAuthPO buildUserAuth(String userId, String email) {
        String salt = IdSeqUtil.genPwdSalt();
        UserAuthPO userAuthPO = new UserAuthPO();
        userAuthPO.setUserId(userId);
        userAuthPO.setIdentityType(IdentityType.EMAIL_PASSWORD.name());
        userAuthPO.setIdentifier(CryptoManager.aesEncrypt(email));
        userAuthPO.setCredential(CryptoManager.encryptPassword(INIT_MD5_PASSWORD, salt));
        userAuthPO.setSalt(salt);
        return userAuthPO;
    }

    String buildEmail(int idx) {
        return String.format(EMAIL_FORMAT, idx);
    }
}
