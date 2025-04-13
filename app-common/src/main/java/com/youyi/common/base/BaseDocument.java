package com.youyi.common.base;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
@Setter
public abstract class BaseDocument implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    private Long gmtCreate;

    /**
     * 修改时间
     */
    private Long gmtModified;
}
