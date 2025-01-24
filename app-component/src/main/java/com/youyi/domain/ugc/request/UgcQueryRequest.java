package com.youyi.domain.ugc.request;

import com.youyi.common.base.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public class UgcQueryRequest extends BasePageRequest {

    private String ugcStatus;

    private String ugcType;

    private String keyword;
}
