package com.youyi.domain.user.repository;

import com.youyi.domain.user.repository.mapper.UserAuthMapper;
import com.youyi.domain.user.repository.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
}
