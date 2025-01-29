package com.youyi.domain.ugc.helper;

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
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
        List<UgcCategoryInfo> ugcCategoryInfoList = allCategories.stream()
            .map(po -> {
                UgcCategoryInfo info = new UgcCategoryInfo();
                info.fillWithUgcCategoryPO(po);
                return info;
            })
            .toList();
        return UgcMetadataDO.of(ugcCategoryInfoList, List.of());
    }

    public UgcMetadataDO queryUgcInterestTag() {
        // 1. 查询所有兴趣标签
        List<UgcTagPO> interestTags = ugcTagRepository.queryByType(UgcTagType.FOR_USER_INTEREST.getType());
        List<UgcTagInfo> tagInfoList = interestTags.stream()
            .map(po -> {
                UgcTagInfo info = new UgcTagInfo();
                info.fillWithUgcTagPO(po);
                return info;
            })
            .toList();
        // 2. 标记用户已经选择的标签
        UserDO currUserInfo = userService.getCurrentUserInfo();
        List<String> personalizedTags = currUserInfo.getPersonalizedTags();
        if (CollectionUtils.isEmpty(personalizedTags)) {
            tagInfoList.forEach(tagInfo -> tagInfo.setSelected(false));
        } else {
            Set<String> userSelectedTags = Sets.newHashSet(personalizedTags);
            tagInfoList.forEach(tagInfo -> tagInfo.setSelected(userSelectedTags.contains(tagInfo.getTagName())));
        }
        return UgcMetadataDO.of(List.of(), tagInfoList);
    }

    public void queryUgcArticleTagWithCursor(UgcMetadataDO ugcMetadataDO) {
        List<UgcTagPO> ugcTagPOList = ugcTagRepository.queryByCursor(ugcMetadataDO.getCursor(), ugcMetadataDO.getSize());
        List<UgcTagInfo> tagInfoList = ugcTagPOList.stream()
            .map(po -> {
                UgcTagInfo info = new UgcTagInfo();
                info.fillWithUgcTagPO(po);
                return info;
            })
            .toList();
        // 回填数据
        ugcMetadataDO.setUgcTagList(tagInfoList);
        // 回填 cursor
        if (CollectionUtils.isNotEmpty(ugcTagPOList)) {
            ugcMetadataDO.setCursor(ugcTagPOList.get(ugcTagPOList.size() - 1).getTagId());
        }
    }

}
