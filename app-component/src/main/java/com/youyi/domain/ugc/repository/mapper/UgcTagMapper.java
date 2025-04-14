package com.youyi.domain.ugc.repository.mapper;

import com.youyi.domain.ugc.repository.po.UgcTagPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper
public interface UgcTagMapper {

    int insertBatch(@Param("tags") List<UgcTagPO> tags);

    List<UgcTagPO> queryByType(@Param("type") Integer tagType);

    List<UgcTagPO> queryAll();

    long count();
}
