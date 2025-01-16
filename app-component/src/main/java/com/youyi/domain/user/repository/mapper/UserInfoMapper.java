package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.UserInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Mapper
public interface UserInfoMapper {

    int insert(UserInfoPO po);

    UserInfoPO queryByEmail(@Param("email") String email);

    UserInfoPO queryByUserId(@Param("userId") String userId);

    int update(UserInfoPO po);
}
