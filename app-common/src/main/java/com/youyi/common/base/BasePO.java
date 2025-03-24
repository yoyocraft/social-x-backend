package com.youyi.common.base;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistence Object
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@Setter
public abstract class BasePO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 删除时间
     */
    private Long deletedAt;

    /**
     * 额外数据
     */
    private String extraData;
}
