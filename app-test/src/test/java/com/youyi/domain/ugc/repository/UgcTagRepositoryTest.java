package com.youyi.domain.ugc.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.wrapper.UgcTagWrapper;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/27
 */
class UgcTagRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UgcTagRepository ugcTagRepository;

    @Test
    void testQueryAll() {
        List<UgcTagPO> tagPOList = ugcTagRepository.queryAll();
        List<UgcTagWrapper> wrappers = tagPOList.stream()
            .map(po -> {
                UgcTagWrapper wrapper = new UgcTagWrapper();
                wrapper.setTagId(po.getTagId());
                wrapper.setTagName(po.getTagName());
                wrapper.setType(po.getType());
                wrapper.setPriority(po.getPriority());
                return wrapper;
            })
            .toList();
        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(wrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }
}
