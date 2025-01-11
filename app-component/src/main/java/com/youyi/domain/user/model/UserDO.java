package com.youyi.domain.user.model;

import com.youyi.common.type.IdentityType;
import com.youyi.common.type.user.GenderType;
import com.youyi.common.type.user.UserRoleType;
import com.youyi.common.type.user.UserStatusType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IvGenerator;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.privacy.CryptoManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.youyi.common.type.ConfigKey.DEFAULT_AVATAR;
import static com.youyi.common.util.RandomGenUtil.genUserId;
import static com.youyi.common.util.RandomGenUtil.genUserNickName;
import static com.youyi.infra.conf.core.SystemConfigService.getStringConfig;
import static com.youyi.infra.privacy.DesensitizedManager.desensitizeEmail;
import static com.youyi.infra.privacy.DesensitizedManager.desensitizeMobile;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Getter
@Setter
public class UserDO {

    private String userId;
    private String originalEmail;
    private String encryptedEmail;
    private IdentityType identityType;
    private String identifier;
    private String credential;
    private Map<String, String> extra;
    private String salt = EMPTY;

    private String encryptedPhone;
    private String originalPhone;
    private String nickName;
    private String avatar;
    private GenderType gender;
    private LocalDate dateOfBirth;
    private UserStatusType status;
    private String bio;
    private List<String> personalizedTags;
    private String location;
    private UserRoleType role;

    public void initUserId() {
        this.userId = genUserId();
    }

    public UserInfoPO buildToSaveUserInfoPO() {
        UserInfoPO userInfoPO = new UserInfoPO();
        userInfoPO.setUserId(userId);
        userInfoPO.setEmail(encryptedEmail);
        userInfoPO.setEmailIv(IvGenerator.generateIv(originalEmail));
        userInfoPO.setPhone(EMPTY);
        userInfoPO.setPhoneIv(EMPTY);
        userInfoPO.setAvatar(getStringConfig(DEFAULT_AVATAR));
        userInfoPO.setNickName(genUserNickName());
        return userInfoPO;
    }

    public UserAuthPO buildToSaveUserAuthPO() {
        UserAuthPO userAuthPO = new UserAuthPO();
        userAuthPO.setUserId(userId);
        userAuthPO.setIdentityType(identityType.name());
        userAuthPO.setIdentifier(CryptoManager.aesEncrypt(identifier));
        userAuthPO.setCredential(credential);
        userAuthPO.setSalt(salt);
        return userAuthPO;
    }

    public UserLoginStateInfo buildLoginStateInfo() {
        return UserLoginStateInfo.builder()
            .bio(bio)
            .role(role)
            .gender(gender)
            .userId(userId)
            .avatar(avatar)
            .location(location)
            .nickName(nickName)
            .status(status)
            .personalizedTags(personalizedTags)
            .desensitizedMobile(desensitizeMobile(originalPhone))
            .desensitizedEmail(desensitizeEmail(originalEmail))
            .build();
    }

    public void fillUserInfo(UserInfoPO userInfoPO) {
        this.userId = userInfoPO.getUserId();
        this.originalEmail = CryptoManager.aesDecrypt(userInfoPO.getEmail(), userInfoPO.getEmailIv());
        this.encryptedEmail = userInfoPO.getEmail();
        this.encryptedPhone = userInfoPO.getPhone();
        this.originalPhone = StringUtils.isNotBlank(encryptedPhone)
            ? CryptoManager.aesDecrypt(userInfoPO.getPhone(), userInfoPO.getPhoneIv()) : EMPTY;
        this.nickName = userInfoPO.getNickName();
        this.avatar = userInfoPO.getAvatar();
        this.gender = GenderType.of(userInfoPO.getGender());
        this.dateOfBirth = userInfoPO.getDateOfBirth();
        this.bio = userInfoPO.getBio();
        this.personalizedTags = GsonUtil.fromJson(userInfoPO.getPersonalizedTags(), List.class, String.class);
        this.location = userInfoPO.getLocation();
        this.status = UserStatusType.of(userInfoPO.getStatus());
        this.role = UserRoleType.of(userInfoPO.getRole());
    }

    public void fillUserInfo(UserLoginStateInfo userLoginStateInfo) {
        this.userId = userLoginStateInfo.getUserId();
        this.nickName = userLoginStateInfo.getNickName();
        this.avatar = userLoginStateInfo.getAvatar();
        this.bio = userLoginStateInfo.getBio();
        this.personalizedTags = userLoginStateInfo.getPersonalizedTags();
        this.location = userLoginStateInfo.getLocation();
        this.gender = userLoginStateInfo.getGender();
        this.role = userLoginStateInfo.getRole();
        this.originalEmail = userLoginStateInfo.getDesensitizedEmail();
        this.originalPhone = userLoginStateInfo.getDesensitizedMobile();
        this.status = userLoginStateInfo.getStatus();
    }
}
