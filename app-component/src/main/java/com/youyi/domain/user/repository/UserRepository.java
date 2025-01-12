package com.youyi.domain.user.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.user.repository.mapper.UserAuthMapper;
import com.youyi.domain.user.repository.mapper.UserInfoMapper;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private final UserAuthMapper userAuthMapper;
    private final UserInfoMapper userInfoMapper;

    public UserInfoPO queryUserInfoByEmail(String email) {
        try {
            checkState(StringUtils.isNotBlank(email));
            return userInfoMapper.queryByEmail(email);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public UserInfoPO queryUserInfoByUserId(String userId) {
        try {
            checkState(StringUtils.isNotBlank(userId));
            return userInfoMapper.queryByUserId(userId);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public UserAuthPO queryUserAuthByIdentityTypeAndIdentifier(String identityType, String identifier) {
        try {
            checkState(StringUtils.isNotBlank(identityType) && StringUtils.isNotBlank(identifier));
            return userAuthMapper.queryByIdentityTypeAndIdentifier(identityType, identifier);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertUserAuth(UserAuthPO userAuthPO) {
        try {
            checkNotNull(userAuthPO);
            int ret = userAuthMapper.insert(userAuthPO);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertOrUpdateUserAuth(UserAuthPO userAuthPO) {
        try {
            checkNotNull(userAuthPO);
            int ret = userAuthMapper.insertOrUpdate(userAuthPO);
            checkState(ret >= SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertUserInfo(UserInfoPO userInfoPO) {
        try {
            checkNotNull(userInfoPO);
            int ret = userInfoMapper.insert(userInfoPO);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
