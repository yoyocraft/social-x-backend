package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.UserAuthPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Mapper
public interface UserAuthMapper {

    int insert(UserAuthPO po);

    int insertOrUpdate(UserAuthPO po);

    UserAuthPO queryByIdentityTypeAndIdentifier(@Param("identityType") String identityType, @Param("identifier") String identifier);

}
