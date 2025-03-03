package com.youyi.domain.ugc.core;

import com.google.gson.reflect.TypeToken;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.cache.CacheKey;
import com.youyi.common.type.ugc.UgcStatisticType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.HotUgcCacheInfo;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRelationshipRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.UgcTagRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import com.youyi.domain.ugc.repository.relation.UgcInteractInfo;
import com.youyi.domain.ugc.repository.relation.UgcNode;
import com.youyi.domain.ugc.util.RecommendUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.infra.cache.manager.CacheManager;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.RepositoryConstant.INIT_QUERY_CURSOR;
import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;
import static com.youyi.common.type.conf.ConfigKey.UGC_TAG_RELATIONSHIP;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofHotUgcListKey;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofUgcCollectCountKey;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofUgcCommentaryCountKey;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofUgcLikeCountKey;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofUgcUserRecommendTagKey;
import static com.youyi.infra.cache.repo.UgcCacheRepo.ofUgcViewCountKey;
import static com.youyi.infra.conf.core.Conf.getStringConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcService {

    private final UgcRepository ugcRepository;
    private final UgcRelationshipRepository ugcRelationshipRepository;

    private final UgcStatisticCacheManager ugcStatisticCacheManager;
    private final CacheManager cacheManager;
    private final UgcTagRepository ugcTagRepository;

    public void publishUgc(UgcDO ugcDO) {
        if (ugcDO.isNew()) {
            // new ugc
            ugcDO.create();
            ugcRepository.saveUgc(ugcDO.buildToSaveUgcDocument());
            return;
        }

        // find by ugcId
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkNotNull(ugcDocument);
        checkStatusValidationBeforeUpdate(ugcDocument);
        checkAuthorization(ugcDO, ugcDocument);
        ugcDO.fillBeforeUpdateWhenPublish(ugcDocument);
        ugcRepository.updateUgc(ugcDO.buildToUpdateUgcDocumentWhenPublish());
    }

    public List<UgcDocument> querySelfUgcWithCursor(UgcDO ugcDO) {
        UserDO author = ugcDO.getAuthor();
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);

        return ugcRepository.queryByStatusForSelfWithCursor(
            ugcDO.getUgcType().name(),
            ugcDO.getStatus().name(),
            author.getUserId(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryWithUgcIdCursor(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryWithTimeCursor(
            ugcDO.getKeyword(),
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            ugcDO.getTags(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryWithUgcIdCursorAndExtraData(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryInfoWithIdCursorAndExtraData(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            ugcDO.getQaStatus(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryWithUgcIdAndAuthorIdCursor(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryUserPageInfoWithTimeCursor(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            ugcDO.getAuthorId(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryByAuthorIdsWithCursor(UgcDO ugcDO, Collection<String> authorIds) {
        if (CollectionUtils.isEmpty(authorIds)) {
            return Collections.emptyList();
        }
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryFollowPageInfoWithTimeCursor(
            SymbolConstant.EMPTY,
            SymbolConstant.EMPTY,
            UgcStatus.PUBLISHED.name(),
            authorIds,
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryByTagWithCursor(UgcDO ugcDO) {
        List<String> tags = ugcDO.getTags();
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }

        long cursor = getTimeCursor(ugcDO);
        return ugcRepository.queryByTagWithTimeCursor(tags, ugcDO.getStatus().name(), cursor, ugcDO.getSize());
    }

    public List<UgcDO> fillAuthorAndCursorInfo(List<UgcDocument> ugcDocumentList, Map<String, UserDO> id2UserInfoMap) {
        // 下一次查询的 cursor
        final String nextCursor = Optional.ofNullable(
            CollectionUtils.isEmpty(ugcDocumentList)
                ? null
                : ugcDocumentList.get(ugcDocumentList.size() - 1).getUgcId()
        ).orElse(SymbolConstant.EMPTY);
        return ugcDocumentList.stream()
            .map(ugcDocument -> {
                UserDO author = id2UserInfoMap.get(ugcDocument.getAuthorId());
                UgcDO ugcDO = new UgcDO();
                ugcDO.fillWithUgcDocument(ugcDocument);
                ugcDO.setAuthor(author);
                ugcDO.setCursor(nextCursor);
                return ugcDO;
            }).toList();
    }

    public List<UgcDO> fillAuthorAndTimeCursor(List<UgcDocument> ugcDocumentList, Map<String, UserDO> id2UserInfoMap, Long nextCursor) {
        return ugcDocumentList.stream()
            .map(ugcDocument -> {
                UserDO author = id2UserInfoMap.get(ugcDocument.getAuthorId());
                UgcDO ugcDO = new UgcDO();
                ugcDO.fillWithUgcDocument(ugcDocument);
                ugcDO.setAuthor(author);
                ugcDO.setTimeCursor(nextCursor);
                return ugcDO;
            }).toList();
    }

    public void incrUgcViewCount(UgcDO ugcDO, UserDO currentUser) {
        boolean canIncr = checkIncrCondition(ugcDO, currentUser);
        if (!canIncr) {
            return;
        }
        // 先保存于内存
        ugcStatisticCacheManager.incrOrDecrUgcViewCount(ugcDO.getUgcId(), true);
    }

    public void likeUgc(UgcDO ugcDO, UserDO currentUser) {
        // 如果需要，创建 UGC 节点
        createUgcIfNeed(ugcDO);

        // 插入喜欢关系
        ugcRelationshipRepository.addLikeRelationship(ugcDO.getUgcId(), currentUser.getUserId());
        // 增加点赞数
        ugcStatisticCacheManager.incrOrDecrUgcLikeCount(ugcDO.getUgcId(), true);
    }

    public void cancelLikeUgc(UgcDO ugcDO, UserDO currentUser) {
        // 不需要创建节点
        // 删除喜欢关系
        ugcRelationshipRepository.deleteLikeRelationship(ugcDO.getUgcId(), currentUser.getUserId());
        // 减少点赞数
        ugcStatisticCacheManager.incrOrDecrUgcLikeCount(ugcDO.getUgcId(), false);
    }

    public void collectUgc(UgcDO ugcDO, UserDO currentUser) {
        // 如果需要，创建 UGC 节点
        createUgcIfNeed(ugcDO);

        // 插入收藏关系
        ugcRelationshipRepository.addCollectRelationship(ugcDO.getUgcId(), currentUser.getUserId());
        // 增加收藏数
        ugcStatisticCacheManager.incrOrDecrUgcCollectCount(ugcDO.getUgcId(), true);
    }

    public void cancelCollectUgc(UgcDO ugcDO, UserDO currentUser) {
        // 不需要创建节点
        // 删除收藏关系
        ugcRelationshipRepository.deleteCollectRelationship(ugcDO.getUgcId(), currentUser.getUserId());
        // 减少收藏数
        ugcStatisticCacheManager.incrOrDecrUgcCollectCount(ugcDO.getUgcId(), false);
    }

    public void createUgcIfNeed(UgcDO ugcDO) {
        Optional<UgcNode> ugcNodeOptional = Optional.ofNullable(ugcRelationshipRepository.findByUgcId(ugcDO.getUgcId()));
        if (ugcNodeOptional.isPresent()) {
            return;
        }
        ugcRelationshipRepository.save(ugcDO.getUgcId());
    }

    public void fillUgcStatistic(List<UgcDO> ugcInfoList) {
        if (CollectionUtils.isEmpty(ugcInfoList)) {
            return;
        }

        List<String> ugcIds = ugcInfoList.stream().map(UgcDO::getUgcId).toList();
        List<UgcStatisticType> statisticTypes = List.of(
            UgcStatisticType.VIEW, UgcStatisticType.LIKE,
            UgcStatisticType.COLLECT, UgcStatisticType.COMMENTARY
        );

        // 一次性查询 Redis
        EnumMap<UgcStatisticType, Map<String, Long>> statisticsMap = ugcStatisticCacheManager.getBatchUgcStatistic(ugcIds, statisticTypes);

        ugcInfoList.forEach(ugcDO -> {
            String ugcId = ugcDO.getUgcId();
            ugcDO.calViewCount(statisticsMap.getOrDefault(UgcStatisticType.VIEW, Map.of()).getOrDefault(ofUgcViewCountKey(ugcId), 0L));
            ugcDO.calLikeCount(statisticsMap.getOrDefault(UgcStatisticType.LIKE, Map.of()).getOrDefault(ofUgcLikeCountKey(ugcId), 0L));
            ugcDO.calCollectCount(statisticsMap.getOrDefault(UgcStatisticType.COLLECT, Map.of()).getOrDefault(ofUgcCollectCountKey(ugcId), 0L));
            ugcDO.calCommentaryCount(statisticsMap.getOrDefault(UgcStatisticType.COMMENTARY, Map.of()).getOrDefault(ofUgcCommentaryCountKey(ugcId), 0L));
        });
    }

    public void filterNoNeedInfoForListPage(List<UgcDO> ugcDOList) {
        ugcDOList.forEach(ugcDO -> {
            // 列表页无需返回 content
            if (UgcType.POST != ugcDO.getUgcType()) {
                ugcDO.setContent(null);
            }
        });
    }

    public List<HotUgcCacheInfo> queryHotUgcFromCache(UgcDO ugcDO) {
        String cacheKey = ofHotUgcListKey(ugcDO.getUgcType().name());
        if (!cacheManager.exists(cacheKey)) {
            // TODO youyi 2025/2/25 待处理
            return Collections.emptyList();
        }

        String cacheInfoJson = cacheManager.getString(cacheKey);
        return GsonUtil.fromJson(cacheInfoJson, List.class, HotUgcCacheInfo.class);
    }

    public List<UgcDO> fillHotUgc(List<HotUgcCacheInfo> hotUgcCacheInfos) {
        if (CollectionUtils.isEmpty(hotUgcCacheInfos)) {
            return Collections.emptyList();
        }

        return hotUgcCacheInfos.stream()
            .map(hotUgcCacheInfo -> {
                UgcDO ugcDO = new UgcDO();
                ugcDO.setUgcId(hotUgcCacheInfo.getUgcId());
                ugcDO.setTitle(hotUgcCacheInfo.getTitle());
                ugcDO.setHotScore(hotUgcCacheInfo.getHotScore());
                ugcDO.setViewCount(hotUgcCacheInfo.getViewCount());
                return ugcDO;
            }).toList();
    }

    public List<UgcDocument> listSelfCollectedUgc(UgcDO ugcDO, UserDO currentUserInfo) {
        List<UgcInteractInfo> ugcInteractInfos = ugcRelationshipRepository.queryCollectedUgcIdsWithCursor(currentUserInfo.getUserId(), ugcDO.getTimeCursor(), ugcDO.getSize());
        if (CollectionUtils.isEmpty(ugcInteractInfos)) {
            return Collections.emptyList();
        }
        // 下一次查询的 cursor
        Long nextCursor = ugcInteractInfos.get(ugcInteractInfos.size() - 1).getSince();
        ugcDO.setTimeCursor(nextCursor);
        // 查询 ugc
        List<String> ugcIds = ugcInteractInfos.stream().map(UgcInteractInfo::getUgcId).toList();
        return ugcRepository.queryByUgcIds(ugcIds);
    }

    private void checkStatusValidationBeforeUpdate(UgcDocument ugcDocument) {
        // 私密的稿件禁止修改
        if (UgcStatus.PRIVATE.name().equals(ugcDocument.getStatus())) {
            throw AppBizException.of(OPERATION_DENIED, "私密稿件禁止修改！");
        }
    }

    private void checkAuthorization(UgcDO ugcDO, UgcDocument ugcDocument) {
        String currentUserId = ugcDO.getAuthor().getUserId();
        String actualAuthorId = ugcDocument.getAuthorId();

        if (!currentUserId.equals(actualAuthorId)) {
            throw AppBizException.of(OPERATION_DENIED, "无权修改！");
        }
    }

    private long getTimeCursor(UgcDO ugcDO) {
        long cursor;
        if (INIT_QUERY_CURSOR.equals(ugcDO.getCursor())) {
            cursor = System.currentTimeMillis();
        } else {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getCursor());
            checkNotNull(ugcDocument);
            cursor = ugcDocument.getGmtModified();
        }
        return cursor;
    }

    private boolean checkIncrCondition(UgcDO ugcDO, UserDO currentUser) {
        // 仅公开稿件才可增加浏览量
        if (ugcDO.getStatus() != UgcStatus.PUBLISHED) {
            return false;
        }

        // 非作者查看可以增加浏览量
        return !ugcDO.getAuthor().getUserId().equals(currentUser.getUserId());
    }

    public List<String> getRecommendTags(UserDO currentUser) {
        List<String> recommendedTags;
        // 0. 先读取缓存数据
        String recommendTagKey = ofUgcUserRecommendTagKey(currentUser.getUserId());
        String recommendTagJson = cacheManager.getString(recommendTagKey);
        if (StringUtils.isNotBlank(recommendTagJson)) {
            recommendedTags = GsonUtil.fromJson(recommendTagJson, List.class, String.class);
            if (!CollectionUtils.isEmpty(recommendedTags)) {
                return recommendedTags;
            }
        }
        // 1. 判断用户是否有感兴趣的标签
        List<String> personalizedTags = currentUser.getPersonalizedTags();
        if (CollectionUtils.isEmpty(personalizedTags)) {
            // 返回空的 Tags, 默认不加入任何的 tag 查询条件
            return Collections.emptyList();
        }
        // TODO youyi 2025/3/3 计算过程加锁
        // 2. 计算 tag
        List<UgcTagPO> ugcTagPOList = ugcTagRepository.queryByType(UgcTagType.FOR_ARTICLE.getType());
        List<String> allTags = ugcTagPOList.stream().map(UgcTagPO::getTagName).toList();
        Map<String, Set<String>> tagRelations = GsonUtil.fromJson(getStringConfig(UGC_TAG_RELATIONSHIP), new TypeToken<>() {
        });
        recommendedTags = RecommendUtil.getTop10RecommendedTags(allTags, personalizedTags, tagRelations);
        // 3. 保存缓存
        recommendTagJson = GsonUtil.toJson(recommendedTags);
        cacheManager.set(recommendTagKey, recommendTagJson, CacheKey.UGC_USER_RECOMMEND_TAG.getTtl());
        return recommendedTags;
    }

    public void fillUgcInteractInfo(List<UgcDO> ugcDOList, UserDO currentUser) {
        if (CollectionUtils.isEmpty(ugcDOList)) {
            return;
        }

        List<String> ugcIds = ugcDOList.stream().map(UgcDO::getUgcId).toList();

        // 批量查询 LIKE 和 COLLECT 关系
        Set<String> likedUgcIds = new HashSet<>(ugcRelationshipRepository.queryLikeRelationships(ugcIds, currentUser.getUserId()));
        Set<String> collectedUgcIds = new HashSet<>(ugcRelationshipRepository.queryCollectRelationships(ugcIds, currentUser.getUserId()));

        // 遍历 UGC 结果集，填充数据
        ugcDOList.forEach(ugcDO -> {
            ugcDO.setLiked(likedUgcIds.contains(ugcDO.getUgcId()));
            ugcDO.setCollected(collectedUgcIds.contains(ugcDO.getUgcId()));
        });
    }
}
