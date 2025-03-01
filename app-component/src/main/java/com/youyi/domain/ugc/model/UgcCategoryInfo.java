package com.youyi.domain.ugc.model;

import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcCategoryInfo {

    private String categoryId;
    private String categoryName;
    private Integer priority;
    private Integer type;
    private UgcCategoryExtraData extraData;

    public void fillWithUgcCategoryPO(UgcCategoryPO po) {
        this.categoryId = po.getCategoryId();
        this.categoryName = po.getCategoryName();
        this.priority = po.getPriority();
        this.type = po.getType();
        this.extraData = Optional.ofNullable(GsonUtil.fromJson(po.getExtraData(), UgcCategoryExtraData.class)).orElseGet(UgcCategoryExtraData::new);
    }
}
