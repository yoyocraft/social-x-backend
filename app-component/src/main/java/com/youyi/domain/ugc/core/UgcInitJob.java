package com.youyi.domain.ugc.core;

import com.youyi.common.wrapper.UgcCategoryWrapper;
import com.youyi.common.wrapper.UgcTagWrapper;
import com.youyi.domain.ugc.repository.UgcCategoryRepository;
import com.youyi.domain.ugc.repository.UgcTagRepository;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import com.youyi.infra.conf.core.Conf;
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
import static com.youyi.common.type.conf.ConfigKey.INNER_UGC_CATEGORIES;
import static com.youyi.common.type.conf.ConfigKey.INNER_UGC_TAGS;
import static com.youyi.common.util.ext.MoreFeatures.runWithCost;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Component
@RequiredArgsConstructor
public class UgcInitJob implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcInitJob.class);

    private final UgcCategoryRepository ugcCategoryRepository;
    private final UgcTagRepository ugcTagRepository;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        initUgcCategories();
        initUgcTags();
    }

    private void initUgcCategories() {
        List<UgcCategoryPO> categoriesFromDB = ugcCategoryRepository.queryAll();
        if (CollectionUtils.isNotEmpty(categoriesFromDB)) {
            LOGGER.info("[UgcInitJob] inner ugc categories already exists, skip init");
            return;
        }
        List<UgcCategoryWrapper> categoryWrappers = Conf.getListConfig(INNER_UGC_CATEGORIES, UgcCategoryWrapper.class);
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
                return po;
            }).toList();

        runWithCost(LOGGER, () -> ugcCategoryRepository.insertBatchCategory(categoryPOList), "initUgcCategories");
    }

    private void initUgcTags() {
        List<UgcTagPO> tagsFromDB = ugcTagRepository.queryAll();
        if (CollectionUtils.isNotEmpty(tagsFromDB)) {
            LOGGER.info("[UgcInitJob] inner ugc tags already exists, skip init");
            return;
        }

        List<UgcTagWrapper> tagWrappers = Conf.getListConfig(INNER_UGC_TAGS, UgcTagWrapper.class);
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

        runWithCost(LOGGER, () -> ugcTagRepository.insertBatchTag(tagPOList), "initUgcTags");
    }
}
