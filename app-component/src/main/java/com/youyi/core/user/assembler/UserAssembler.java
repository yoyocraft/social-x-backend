package com.youyi.core.user.assembler;

import com.youyi.core.user.domain.UserDO;
import com.youyi.core.user.param.UserAddParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
@Mapper
public interface UserAssembler {

    UserAssembler INSTANCE = Mappers.getMapper(UserAssembler.class);

    @Mappings({
        @Mapping(source = "username", target = "name"),
    })
    UserDO toDO(UserAddParam param);
}
