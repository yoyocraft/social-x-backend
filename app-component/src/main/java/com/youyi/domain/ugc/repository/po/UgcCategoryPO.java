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
public class UgcCategoryPO extends BasePO {

    private String categoryId;

    private String categoryName;

    private String creatorId;

    private Integer priority;
}
