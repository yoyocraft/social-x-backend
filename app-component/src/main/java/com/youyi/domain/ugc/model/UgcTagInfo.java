package com.youyi.domain.ugc.model;

import com.youyi.common.type.ugc.UgcTagType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcTagInfo {

    private String tagId;
    private String tagName;
    private UgcTagType type;
    private Integer priority;
    private Boolean selected;
}
