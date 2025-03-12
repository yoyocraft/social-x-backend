package com.youyi.domain.task.repository.mapper;

import com.youyi.domain.task.repository.po.SysTaskPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Mapper
public interface SysTaskMapper {

    int insert(SysTaskPO po);

    int insertBatch(@Param("poList") List<SysTaskPO> poList);

    int updateStatus(@Param("taskIds") List<String> taskIds, @Param("taskStatus") String taskStatus);

    List<SysTaskPO> queryByTypeAndStatusWithCursor(
        @Param("taskType") String taskType,
        @Param("taskStatus") List<String> taskStatus,
        @Param("cursor") Long cursor,
        @Param("size") Integer size,
        @Param("withTimeInterval") Boolean withTimeInterval
    );
}
