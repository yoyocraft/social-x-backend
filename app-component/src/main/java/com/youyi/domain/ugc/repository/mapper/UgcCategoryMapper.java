package com.youyi.domain.ugc.repository.mapper;

import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Mapper
public interface UgcCategoryMapper {

    int insert(UgcCategoryPO po);

    int insertBatch(@Param("categories") List<UgcCategoryPO> poList);

    List<UgcCategoryPO> queryAll();
}
