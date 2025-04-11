package com.youyi.domain.ugc.core;

import com.youyi.common.wrapper.UgcCategoryWrapper;
import com.youyi.common.wrapper.UgcTagWrapper;
import com.youyi.domain.ugc.repository.UgcCategoryRepository;
import com.youyi.domain.ugc.repository.UgcTagRepository;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.SystemConstant.SYSTEM_OPERATOR_ID;
import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.infra.conf.core.Conf.getListConfig;
import static com.youyi.infra.conf.core.ConfigKey.SYSTEM_PRESET_UGC_CATEGORY;
import static com.youyi.infra.conf.core.ConfigKey.SYSTEM_PRESET_UGC_TAG;
import static com.youyi.infra.conf.util.CommonConfUtil.hasScheduleOn;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Component
@RequiredArgsConstructor
public class UgcMetadataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UgcMetadataInitializer.class);

    private final UgcCategoryRepository ugcCategoryRepository;
    private final UgcTagRepository ugcTagRepository;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        if (!hasScheduleOn()) {
            return;
        }
        initUgcCategories();
        initUgcTags();
    }

    private void initUgcCategories() {
        long count = ugcCategoryRepository.count();
        if (count > 0) {
            logger.info("[UgcMetadataInitializer] inner ugc categories already exists, skip init");
            return;
        }
        List<UgcCategoryWrapper> categoryWrappers = getListConfig(SYSTEM_PRESET_UGC_CATEGORY, UgcCategoryWrapper.class);
        checkState(
            CollectionUtils.isNotEmpty(categoryWrappers),
            "inner ugc categories is empty, please check INNER_UGC_CATEGORIES config"
        );
        List<UgcCategoryPO> categoryPOList = categoryWrappers.stream()
            .map(wrapper -> {
                UgcCategoryPO po = new UgcCategoryPO();
                po.setCategoryId(wrapper.getCategoryId());
                po.setCategoryName(wrapper.getCategoryName());
                po.setCreatorId(SYSTEM_OPERATOR_ID);
                po.setPriority(wrapper.getPriority());
                po.setType(wrapper.getType());
                po.setExtraData(wrapper.getExtraData());
                return po;
            }).toList();

        runWithCost(logger, () -> ugcCategoryRepository.insertBatchCategory(categoryPOList), "initUgcCategories");
    }

    private void initUgcTags() {
        long count = ugcTagRepository.count();
        if (count > 0) {
            logger.info("[UgcMetadataInitializer] inner ugc tags already exists, skip init");
            return;
        }

        List<UgcTagWrapper> tagWrappers = getListConfig(SYSTEM_PRESET_UGC_TAG, UgcTagWrapper.class);
        checkState(
            CollectionUtils.isNotEmpty(tagWrappers),
            "inner ugc tags is empty, please check INNER_UGC_TAGS config"
        );

        List<UgcTagPO> tagPOList = tagWrappers.stream()
            .map(wrapper -> {
                UgcTagPO po = new UgcTagPO();
                po.setTagId(wrapper.getTagId());
                po.setTagName(wrapper.getTagName());
                po.setCreatorId(SYSTEM_OPERATOR_ID);
                po.setPriority(wrapper.getPriority());
                po.setType(wrapper.getType());
                return po;
            }).toList();

        runWithCost(logger, () -> ugcTagRepository.insertBatchTag(tagPOList), "initUgcTags");
    }
}
