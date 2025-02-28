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
        List<String> topics = List.of("闲聊", "学习打卡", "学习指南", "项目", "求职", "面试", "职场", "资源", "学习总结", "知识碎片");
        List<UgcCategoryPO> pos = topics.stream()
            .map(topic -> {
                UgcCategoryPO po = new UgcCategoryPO();
                po.setCategoryId(IdSeqUtil.genUgcCategoryId());
                po.setCategoryName(topic);
                po.setCreatorId("-1");
                po.setPriority(priority.getAndIncrement());
                po.setType(UgcCategoryType.POST_TOPIC.getType());
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
                wrapper.setIcon(po.getIcon());
                wrapper.setType(po.getType());
                return wrapper;
            })
            .toList();
        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(wrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }
}
