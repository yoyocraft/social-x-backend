package com.youyi.domain.ugc.repository.po;

import com.youyi.common.base.BasePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcTagPO extends BasePO {

    private String tagId;
    private String tagName;
    private Integer priority;
    private Integer type;
    private String creatorId;

}
