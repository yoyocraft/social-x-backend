package com.youyi.domain.ugc.core;

import com.google.gson.reflect.TypeToken;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.ugc.model.HotUgcCacheInfo;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcRelationshipRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.UgcTagRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import com.youyi.domain.ugc.repository.relation.UgcInteractInfo;
import com.youyi.domain.ugc.repository.relation.UgcNode;
import com.youyi.domain.ugc.type.UgcStatisticType;
import com.youyi.domain.ugc.type.UgcStatus;
import com.youyi.domain.ugc.type.UgcTagType;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.domain.ugc.util.RecommendUtil;
import com.youyi.domain.ugc.util.UgcContentUtil;
import com.youyi.domain.user.model.UserDO;
import com.youyi.infra.ai.AiClient;
import com.youyi.infra.cache.CacheKey;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.sse.SseEmitter;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.RepositoryConstant.INIT_QUERY_CURSOR;
import static com.youyi.common.constant.SystemConstant.DEFAULT_KEY;
import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofHotUgcListKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcCollectCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcCommentaryCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcLikeCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcUserRecommendTagKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcViewCountKey;
import static com.youyi.infra.conf.core.Conf.getMapConfig;
import static com.youyi.infra.conf.core.Conf.getStringConfig;
import static com.youyi.infra.conf.core.ConfigKey.AI_UGC_VIEW_SUMMARY_PROMPT;
import static com.youyi.infra.conf.core.ConfigKey.DEFAULT_RECOMMEND_TAG;
import static com.youyi.infra.conf.core.ConfigKey.UGC_TAG_RELATIONSHIP;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcService {

    private static final ReentrantLock calRecommendTagLock = new ReentrantLock();

    private final UgcRepository ugcRepository;
    private final UgcRelationshipRepository ugcRelationshipRepository;
    private final UgcTagRepository ugcTagRepository;

    private final UgcStatisticCacheManager ugcStatisticCacheManager;
    private final CacheRepository cacheRepository;

    private final AiClient aiClient;

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
        checkAuthorization(ugcDO, ugcDocument);
        ugcDO.fillBeforeUpdateWhenPublish(ugcDocument);
        ugcRepository.updateUgc(ugcDO.buildToUpdateUgcDocumentWhenPublish());
    }

    public List<UgcDocument> querySelfUgc(UgcDO ugcDO) {
        String authorId = ugcDO.getAuthorId();
        // 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        return ugcRepository.querySelfUgc(ugcDO.getUgcType().name(), ugcDO.getStatus().name(), authorId, cursor, ugcDO.getSize());
    }

    public List<UgcDocument> listTimelineUgcFeed(UgcDO ugcDO) {
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

    public List<UgcDocument> listQuestions(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryByExtraDataWithTimeCursor(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            ugcDO.getQaStatus(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> queryUserUgc(UgcDO ugcDO) {
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryByAuthorWithTimeCursor(
            ugcDO.getCategoryId(),
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            ugcDO.getAuthorId(),
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> listFollowUgcFeed(UgcDO ugcDO, Collection<String> authorIds) {
        if (CollectionUtils.isEmpty(authorIds)) {
            return Collections.emptyList();
        }
        // 1. 根据 cursor 查询 gmt_modified 作为查询游标
        long cursor = getTimeCursor(ugcDO);
        // 2. 查询
        return ugcRepository.queryByAuthorWithTimeCursor(
            SymbolConstant.EMPTY,
            ugcDO.getUgcType().name(),
            UgcStatus.PUBLISHED.name(),
            authorIds,
            cursor,
            ugcDO.getSize()
        );
    }

    public List<UgcDocument> listRecommendUgcFeed(UgcDO ugcDO) {
        List<String> tags = ugcDO.getTags();
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }

        long cursor = getTimeCursor(ugcDO);
        return ugcRepository.queryByTagWithTimeCursor(tags, ugcDO.getCategoryId(), ugcDO.getStatus().name(), cursor, ugcDO.getSize());
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
        ugcStatisticCacheManager.incrOrDecrUgcViewCount(ugcDO.getUgcId());
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
        if (!cacheRepository.exists(cacheKey)) {
            return Collections.emptyList();
        }

        String cacheInfoJson = cacheRepository.getString(cacheKey);
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
        return ugcRepository.queryBatchByUgcId(ugcIds);
    }

    public void aiGenerateSummary(UgcDO ugcDO, SseEmitter sseEmitter) {
        String ugcPrompt = UgcContentUtil.polishGenUgcSummaryUserMessage(ugcDO.getTitle(), ugcDO.getContent());

        StringBuilder summaryBuilder = new StringBuilder();
        StringBuilder lineBuilder = new StringBuilder();

        // 请求 AI 生成摘要
        Flowable<ModelData> dataFlowable = aiClient.doStreamStableRequest(getStringConfig(AI_UGC_VIEW_SUMMARY_PROMPT), ugcPrompt);

        dataFlowable
            .observeOn(Schedulers.io())
            .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
            .filter(StringUtils::isNotBlank)
            .flatMap(message -> {
                List<Character> characters = new ArrayList<>();
                for (char c : message.toCharArray()) {
                    characters.add(c);
                }
                return Flowable.fromIterable(characters);
            })
            .doOnNext(c -> {
                lineBuilder.append(c);
                if (c == SymbolConstant.NEW_LINE_CHAR) {
                    String line = UgcContentUtil.polishSummaryLine(lineBuilder);

                    if (!line.isEmpty()) {
                        sseEmitter.send(line);
                        summaryBuilder.append(line).append(SymbolConstant.NEW_LINE);
                    }
                    lineBuilder.setLength(0);
                }
            })
            .doOnError(sseEmitter::completeWithError)
            .doOnComplete(() -> {
                UgcExtraData extraData = Optional.ofNullable(ugcDO.getExtraData()).orElseGet(UgcExtraData::new);
                extraData.setUgcSummary(summaryBuilder.toString());
                ugcDO.setExtraData(extraData);
                ugcRepository.updateUgc(ugcDO.buildToUpdateUgcDocumentWhenPublish());
                sseEmitter.complete();
            })
            .subscribe();
    }

    public List<String> getRecommendTags(UserDO currentUser) {
        List<String> recommendedTags;
        // 0. 先读取缓存数据
        String recommendTagKey = ofUgcUserRecommendTagKey(currentUser.getUserId());
        String recommendTagJson = cacheRepository.getString(recommendTagKey);
        if (StringUtils.isNotBlank(recommendTagJson)) {
            recommendedTags = GsonUtil.fromJson(recommendTagJson, List.class, String.class);
            if (CollectionUtils.isNotEmpty(recommendedTags)) {
                return recommendedTags;
            }
        }
        // 1. 判断用户是否有感兴趣的标签
        List<String> personalizedTags = currentUser.getPersonalizedTags();
        if (CollectionUtils.isEmpty(personalizedTags)) {
            // 返回空的 Tags, 默认不加入任何的 tag 查询条件
            return Collections.emptyList();
        }
        if (!calRecommendTagLock.tryLock()) {
            return Collections.emptyList();
        }
        try {
            // 2. 计算 tag
            List<UgcTagPO> ugcTagPOList = ugcTagRepository.queryByType(UgcTagType.FOR_ARTICLE.getType());
            List<String> allTags = ugcTagPOList.stream().map(UgcTagPO::getTagName).toList();
            Map<String, Set<String>> tagRelations = GsonUtil.fromJson(getStringConfig(UGC_TAG_RELATIONSHIP), new TypeToken<>() {
            });
            recommendedTags = RecommendUtil.getTop10RecommendedTags(allTags, personalizedTags, tagRelations);
            // 3. 保存缓存
            recommendTagJson = GsonUtil.toJson(recommendedTags);
            cacheRepository.set(recommendTagKey, recommendTagJson, CacheKey.UGC_USER_RECOMMEND_TAG.getTtl());
            return recommendedTags;
        } finally {
            calRecommendTagLock.unlock();
        }
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

    public void fillRecommendTags(UgcDO ugcDO, List<String> systemRecommendTags) {
        if (StringUtils.isBlank(ugcDO.getCategoryId())) {
            ugcDO.setTags(systemRecommendTags);
            return;
        }
        Set<String> recommendTags = new HashSet<>(systemRecommendTags);
        Map<String, String> defaultRecommendTagMap = getMapConfig(DEFAULT_RECOMMEND_TAG, String.class, String.class);
        String defaultRecommendTag = defaultRecommendTagMap.getOrDefault(ugcDO.getCategoryId(), defaultRecommendTagMap.get(DEFAULT_KEY));
        if (StringUtils.isBlank(defaultRecommendTag) || recommendTags.contains(defaultRecommendTag)) {
            ugcDO.setTags(new ArrayList<>(recommendTags));
            return;
        }
        recommendTags.add(defaultRecommendTag);
        ugcDO.setTags(new ArrayList<>(recommendTags));
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

    public void fillContentStatistic(UgcDO ugcDO, List<UgcDocument> ugcDocumentList, List<String> collectedUgcIds) {
        Map<String, List<UgcDocument>> typeUgcMap = ugcDocumentList.stream().collect(Collectors.groupingBy(UgcDocument::getType));
        ugcDO.setArticleCount((long) typeUgcMap.getOrDefault(UgcType.ARTICLE.name(), Collections.emptyList()).size());
        ugcDO.setPostCount((long) typeUgcMap.getOrDefault(UgcType.POST.name(), Collections.emptyList()).size());
        ugcDO.setQuestionCount((long) typeUgcMap.getOrDefault(UgcType.QUESTION.name(), Collections.emptyList()).size());
        ugcDO.setCollectCount((long) collectedUgcIds.size());
    }

    public void fillActivityStatistic(UgcDO ugcDO, List<UgcDocument> ugcDocumentList, List<CommentaryDocument> commentaryDocumentList) {
        ugcDO.setCommentaryCount((long) commentaryDocumentList.size());
        ugcDocumentList.stream().map(UgcDocument::getLikeCount)
            .reduce(Long::sum)
            .ifPresent(ugcDO::setLikeCount);
    }

}
