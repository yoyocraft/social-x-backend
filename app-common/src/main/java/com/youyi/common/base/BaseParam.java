package com.youyi.common.base;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Frontend --> Backend
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@Setter
public class BaseParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求唯一标识，防重，幂等校验
     */
    private String uuid;
}
