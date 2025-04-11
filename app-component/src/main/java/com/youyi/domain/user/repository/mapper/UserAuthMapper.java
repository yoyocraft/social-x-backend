package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.UserAuthPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/07
 */
@Mapper
public interface UserAuthMapper {

    int insert(UserAuthPO po);

    UserAuthPO queryByIdentityTypeAndIdentifier(@Param("identityType") String identityType, @Param("identifier") String identifier);

    int update(UserAuthPO po);

    // for test
    int insertBatch(List<UserAuthPO> pos);

}
