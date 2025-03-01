package com.youyi.domain.ugc.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.ugc.UgcCategoryType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.common.wrapper.UgcCategoryWrapper;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/27
 */
class UgcCategoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UgcCategoryRepository ugcCategoryRepository;

    @Test
    void testInsertBatch() {
        AtomicInteger priority = new AtomicInteger(0);
        List<String> categories = List.of("学习指南", "求职指南", "Offer选择", "职场指南", "Bug解决", "技术知识", "求资源", "其他");
        List<UgcCategoryPO> pos = categories.stream()
            .map(category -> {
                UgcCategoryPO po = new UgcCategoryPO();
                po.setCategoryId(IdSeqUtil.genUgcCategoryId());
                po.setCategoryName(category);
                po.setCreatorId("-1");
                po.setPriority(priority.getAndIncrement());
                po.setType(UgcCategoryType.QUESTION_CATEGORY.getType());
                return po;
            })
            .toList();
        ugcCategoryRepository.insertBatchCategory(pos);
    }

    @Test
    void testQueryAll() {
        List<UgcCategoryPO> categoryPOList = ugcCategoryRepository.queryAll();
        List<UgcCategoryWrapper> wrappers = categoryPOList.stream()
            .map(po -> {
                UgcCategoryWrapper wrapper = new UgcCategoryWrapper();
                wrapper.setCategoryId(po.getCategoryId());
                wrapper.setCategoryName(po.getCategoryName());
                wrapper.setPriority(po.getPriority());
                wrapper.setType(po.getType());
                wrapper.setExtraData(po.getExtraData());
                return wrapper;
            })
            .toList();
        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(wrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }
}
