package com.youyi.domain.user.model;

import com.youyi.common.type.BizType;
import com.youyi.common.type.user.IdentityType;
import com.youyi.common.type.user.UserRoleType;
import com.youyi.common.type.user.UserStatusType;
import com.youyi.common.type.user.WorkDirectionType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.common.util.crypto.IvGenerator;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import com.youyi.infra.privacy.CryptoManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.youyi.common.type.conf.ConfigKey.DEFAULT_AVATAR;
import static com.youyi.common.util.IdSeqUtil.genUserId;
import static com.youyi.common.util.IdSeqUtil.genUserNickname;
import static com.youyi.infra.conf.core.Conf.getStringConfig;
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
    private String nickname;
    private String avatar;
    private String workStartTime;
    private WorkDirectionType workDirection;
    private String bio;
    private List<String> personalizedTags;
    private String jobTitle;
    private String company;

    private UserStatusType status;
    private UserRoleType role;

    private LocalDateTime joinTime;

    private Integer followerCount;
    private Integer followingCount;

    /**
     * for captcha check
     */
    private BizType bizType;
    private String verifyCaptchaToken;
    private String toVerifiedCaptcha;

    /**
     * for set password
     */
    private String newPassword;
    private String confirmPassword;

    /**
     * for follow user
     */
    private String followingUserId;
    private Boolean followFlag;

    private String cursor;
    private Integer size;

    private Boolean hasFollowed;

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
        userInfoPO.setNickname(genUserNickname());
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
            .userId(userId)
            .avatar(avatar)
            .status(status)
            .company(company)
            .nickname(nickname)
            .jobTitle(jobTitle)
            .joinTime(joinTime)
            .workStartTime(workStartTime)
            .workDirection(workDirection)
            .personalizedTags(personalizedTags)
            .desensitizedEmail(desensitizeEmail(originalEmail))
            .desensitizedMobile(desensitizeMobile(originalPhone))
            .build();
    }

    public UserInfoPO buildToUpdateUserInfoPO() {
        UserInfoPO userInfoPO = new UserInfoPO();
        userInfoPO.setUserId(this.userId);
        userInfoPO.setNickname(this.nickname);
        userInfoPO.setAvatar(this.avatar);
        userInfoPO.setWorkStartTime(this.workStartTime);
        userInfoPO.setWorkDirection(this.workDirection.getCode());
        userInfoPO.setBio(this.bio);
        userInfoPO.setPersonalizedTags(GsonUtil.toJson(this.personalizedTags));
        userInfoPO.setJobTitle(this.jobTitle);
        userInfoPO.setCompany(this.company);
        return userInfoPO;
    }

    public void fillUserInfo(UserInfoPO userInfoPO) {
        this.userId = userInfoPO.getUserId();
        this.originalEmail = CryptoManager.aesDecrypt(userInfoPO.getEmail(), userInfoPO.getEmailIv());
        this.encryptedEmail = userInfoPO.getEmail();
        this.encryptedPhone = userInfoPO.getPhone();
        this.originalPhone = StringUtils.isNotBlank(userInfoPO.getPhone())
            ? CryptoManager.aesDecrypt(userInfoPO.getPhone(), userInfoPO.getPhoneIv())
            : EMPTY;
        this.nickname = userInfoPO.getNickname();
        this.avatar = userInfoPO.getAvatar();
        this.bio = userInfoPO.getBio();
        this.personalizedTags = GsonUtil.fromJson(userInfoPO.getPersonalizedTags(), List.class, String.class);
        this.workStartTime = userInfoPO.getWorkStartTime();
        this.workDirection = WorkDirectionType.fromCode(userInfoPO.getWorkDirection());
        this.jobTitle = userInfoPO.getJobTitle();
        this.company = userInfoPO.getCompany();

        this.status = UserStatusType.of(userInfoPO.getStatus());
        this.role = UserRoleType.of(userInfoPO.getRole());
        this.joinTime = userInfoPO.getGmtCreate();
    }

    public void fillUserInfo(UserLoginStateInfo userLoginStateInfo) {
        this.userId = userLoginStateInfo.getUserId();
        this.nickname = userLoginStateInfo.getNickname();
        this.avatar = userLoginStateInfo.getAvatar();
        this.bio = userLoginStateInfo.getBio();
        this.personalizedTags = userLoginStateInfo.getPersonalizedTags();
        this.workStartTime = userLoginStateInfo.getWorkStartTime();
        this.workDirection = userLoginStateInfo.getWorkDirection();
        this.jobTitle = userLoginStateInfo.getJobTitle();
        this.company = userLoginStateInfo.getCompany();
        this.role = userLoginStateInfo.getRole();
        this.originalEmail = userLoginStateInfo.getDesensitizedEmail();
        this.originalPhone = userLoginStateInfo.getDesensitizedMobile();
        this.status = userLoginStateInfo.getStatus();
        this.joinTime = userLoginStateInfo.getJoinTime();
    }

    public void initSalt() {
        this.salt = IdSeqUtil.genPwdSalt();
    }

    public void encryptPwd() {
        this.credential = CryptoManager.encryptPassword(this.newPassword, this.salt);
    }

    public void preSetPwd() {
        this.identifier = this.originalEmail;
        this.identityType = IdentityType.EMAIL_PASSWORD;
    }

    public boolean isAdmin() {
        return UserRoleType.ADMIN == this.role;
    }

    public static UserDO of(String userId) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        return userDO;
    }
}
