package com.youyi.domain.media.repository.mapper;

import com.youyi.domain.media.repository.po.MediaResourcePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Mapper
public interface MediaResourceMapper {

    int insert(MediaResourcePO po);
}
