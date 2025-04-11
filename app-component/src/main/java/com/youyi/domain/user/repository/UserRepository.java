package com.youyi.domain.user.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.user.repository.mapper.UserAuthMapper;
import com.youyi.domain.user.repository.mapper.UserInfoMapper;
import com.youyi.domain.user.repository.po.UserAuthPO;
import com.youyi.domain.user.repository.po.UserInfoPO;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Repository
@RequiredArgsConstructor
public class UserRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final UserAuthMapper userAuthMapper;
    private final UserInfoMapper userInfoMapper;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MYSQL;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MYSQL_ERROR;
    }

    public UserInfoPO queryUserInfoByEmail(String email) {
        checkState(StringUtils.isNotBlank(email));
        return executeWithExceptionHandling(() -> userInfoMapper.queryByEmail(email));

    }

    public UserInfoPO queryUserInfoByUserId(String userId) {
        checkState(StringUtils.isNotBlank(userId));
        return executeWithExceptionHandling(() -> userInfoMapper.queryByUserId(userId));
    }

    public UserAuthPO queryUserAuthByIdentityTypeAndIdentifier(String identityType, String identifier) {
        checkState(StringUtils.isNotBlank(identityType) && StringUtils.isNotBlank(identifier));
        return executeWithExceptionHandling(() -> userAuthMapper.queryByIdentityTypeAndIdentifier(identityType, identifier));
    }

    public void insertUserAuth(UserAuthPO userAuthPO) {
        checkNotNull(userAuthPO);
        int ret = executeWithExceptionHandling(() -> userAuthMapper.insert(userAuthPO));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateUserAuth(UserAuthPO userAuthPO) {
        checkState(
            Objects.nonNull(userAuthPO)
                && StringUtils.isNotBlank(userAuthPO.getIdentityType())
                && StringUtils.isNotBlank(userAuthPO.getIdentifier())
        );
        UserAuthPO po = executeWithExceptionHandling(() -> userAuthMapper.queryByIdentityTypeAndIdentifier(userAuthPO.getIdentityType(), userAuthPO.getIdentifier()));
        if (Objects.isNull(po)) {
            int ret = executeWithExceptionHandling(() -> userAuthMapper.insert(userAuthPO));
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
            return;
        }
        int ret = executeWithExceptionHandling(() -> userAuthMapper.update(userAuthPO));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    public void insertUserInfo(UserInfoPO userInfoPO) {
        checkNotNull(userInfoPO);
        int ret = executeWithExceptionHandling(() -> userInfoMapper.insert(userInfoPO));
        checkState(ret == SINGLE_DML_AFFECTED_ROWS);
    }

    public void editUserInfo(UserInfoPO userInfoPO) {
        checkNotNull(userInfoPO);
        executeWithExceptionHandling(() -> userInfoMapper.update(userInfoPO));
    }

    public List<UserInfoPO> queryUserInfoByUserIds(Collection<String> userIds) {
        checkState(CollectionUtils.isNotEmpty(userIds));
        return executeWithExceptionHandling(() -> userInfoMapper.queryBatchByUserId(userIds));
    }

    public List<UserInfoPO> querySuggestedUsers(UserInfoPO userInfoPO) {
        checkNotNull(userInfoPO);
        return executeWithExceptionHandling(() -> userInfoMapper.querySuggestedUsers(userInfoPO));
    }

    // for test

    public void insertUserInfoBatch(List<UserInfoPO> userInfoPOs) {
        checkState(CollectionUtils.isNotEmpty(userInfoPOs));
        int ret = executeWithExceptionHandling(() -> userInfoMapper.insertBatch(userInfoPOs));
        checkState(ret == userInfoPOs.size());
    }

    public void insertUserAuthBatch(List<UserAuthPO> userAuthPOs) {
        checkState(CollectionUtils.isNotEmpty(userAuthPOs));
        int ret = executeWithExceptionHandling(() -> userAuthMapper.insertBatch(userAuthPOs));
        checkState(ret == userAuthPOs.size());
    }
}
