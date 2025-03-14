package com.youyi.domain.media.repository.po;

import com.youyi.common.base.BasePO;
import com.youyi.domain.media.type.MediaSource;
import com.youyi.domain.media.type.ResourceType;
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
