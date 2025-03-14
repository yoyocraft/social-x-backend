package com.youyi.domain.ugc.model;

import com.youyi.domain.ugc.type.UgcTagType;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
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

    public void fillWithUgcTagPO(UgcTagPO po) {
        this.tagId = po.getTagId();
        this.tagName = po.getTagName();
        this.type = UgcTagType.of(po.getType());
        this.priority = po.getPriority();
    }
}
