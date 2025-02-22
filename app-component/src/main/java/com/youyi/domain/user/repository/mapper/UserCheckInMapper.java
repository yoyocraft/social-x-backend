package com.youyi.domain.user.repository.mapper;

import com.youyi.domain.user.repository.po.UserCheckInPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/22
 */
@Mapper
public interface UserCheckInMapper {

    int insert(UserCheckInPO po);
}
