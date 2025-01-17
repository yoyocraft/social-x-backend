package com.youyi.domain.media.repository.po;

import com.youyi.common.base.BasePO;
import com.youyi.common.type.media.MediaSource;
import com.youyi.common.type.media.ResourceType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
@Setter
public class MediaResourcePO extends BasePO {

    private String resourceKey;

    /**
     * @see ResourceType
     */
    private String resourceType;

    private String resourceUrl;

    /**
     * @see MediaSource
     */
    private String source;

    private String creatorId;
}
