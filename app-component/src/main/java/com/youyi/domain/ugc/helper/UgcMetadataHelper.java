package com.youyi.domain.ugc.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.domain.ugc.model.UgcCategoryInfo;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.domain.ugc.model.UgcTagInfo;
import com.youyi.domain.ugc.repository.UgcCategoryRepository;
import com.youyi.domain.ugc.repository.UgcTagRepository;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import static com.youyi.domain.ugc.assembler.UgcMetadataAssembler.UGC_METADATA_ASSEMBLER;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Service
@RequiredArgsConstructor
public class UgcMetadataHelper {

    private final UgcTagRepository ugcTagRepository;
    private final UgcCategoryRepository ugcCategoryRepository;

    private final UserService userService;

    public UgcMetadataDO queryUgcCategory() {
        List<UgcCategoryPO> allCategories = ugcCategoryRepository.queryAll();
        List<UgcCategoryInfo> ugcCategoryInfoList = allCategories.stream().map(UGC_METADATA_ASSEMBLER::toCategoryInfo).toList();
        return UgcMetadataDO.of(ugcCategoryInfoList, Lists.newArrayList());
    }

    public UgcMetadataDO queryUgcInterestTag() {
        // 1. 查询所有兴趣标签
        List<UgcTagPO> interestTags = ugcTagRepository.queryByType(UgcTagType.FOR_USER_INTEREST.getType());
        List<UgcTagInfo> tagInfoList = interestTags.stream().map(UGC_METADATA_ASSEMBLER::toTagInfo).toList();
        // 2. 标记用户已经选择的标签
        UserDO currUserInfo = userService.getCurrentUserInfo();
        List<String> personalizedTags = currUserInfo.getPersonalizedTags();
        if (CollectionUtils.isEmpty(personalizedTags)) {
            tagInfoList.forEach(tagInfo -> tagInfo.setSelected(false));
        } else {
            HashSet<String> userSelectedTags = Sets.newHashSet(personalizedTags);
            tagInfoList.forEach(tagInfo -> tagInfo.setSelected(userSelectedTags.contains(tagInfo.getTagName())));
        }
        return UgcMetadataDO.of(Lists.newArrayList(), tagInfoList);
    }

    public void queryUgcArticleTagWithCursor(UgcMetadataDO ugcMetadataDO) {
        List<UgcTagPO> ugcTagPOList = ugcTagRepository.queryByCursor(ugcMetadataDO.getCursor(), ugcMetadataDO.getSize());
        List<UgcTagInfo> tagInfoList = ugcTagPOList.stream().map(UGC_METADATA_ASSEMBLER::toTagInfo).toList();
        // 回填数据
        ugcMetadataDO.setUgcTagList(tagInfoList);
        // 回填 cursor
        if (CollectionUtils.isNotEmpty(ugcTagPOList)) {
            ugcMetadataDO.setCursor(ugcTagPOList.get(ugcTagPOList.size() - 1).getId() + 1);
        }
    }

}
