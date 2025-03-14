package com.youyi.domain.ugc.helper;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.exception.AppBizException;
import com.youyi.domain.ugc.type.UgcInteractionType;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.domain.notification.core.NotificationManager;
import com.youyi.domain.task.core.SysTaskService;
import com.youyi.domain.task.type.TaskType;
import com.youyi.domain.ugc.core.UgcService;
import com.youyi.domain.ugc.core.UgcTpeContainer;
import com.youyi.domain.ugc.model.HotUgcCacheInfo;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcCategoryRepository;
import com.youyi.domain.ugc.repository.UgcRelationshipRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
import com.youyi.domain.ugc.util.UgcContentUtil;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import com.youyi.infra.sse.SseEmitter;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.type.ReturnCode.OPERATION_DENIED;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Service
@RequiredArgsConstructor
public class UgcHelper {

    private final UgcTpeContainer ugcTpeContainer;
    private final SysTaskService sysTaskService;
    private final NotificationManager notificationManager;

    private final UserService userService;
    private final UgcService ugcService;

    private final UgcRepository ugcRepository;
    private final UgcCategoryRepository ugcCategoryRepository;
    private final UgcRelationshipRepository ugcRelationshipRepository;

    public void publishUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthor(ugcDO);
        fillUgcInfo(ugcDO);
        ugcService.publishUgc(ugcDO);
    }

    public void deleteUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthor(ugcDO);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkSelfAuthor(ugcDO, ugcDocument);
        ugcRepository.deleteUgc(ugcDO.getUgcId());
        // 异步写入本地 SysTask，异步处理删除后续
        ugcTpeContainer.getUgcSysTaskExecutor().execute(() -> sysTaskService.saveCommonSysTask(ugcDO.getUgcId(), TaskType.UGC_DELETE_EVENT));
    }

    public List<UgcDO> listSelfUgc(UgcDO ugcDO) {
        // 1. 查询作者信息
        fillCurrUserAsAuthor(ugcDO);
        // 2. 查询
        List<UgcDocument> ugcDocuments = ugcService.querySelfUgc(ugcDO);
        // 3. 封装作者信息和游标信息
        UserDO author = ugcDO.getAuthor();
        List<UgcDO> ugcInfoList = ugcService.fillAuthorAndCursorInfo(ugcDocuments, Map.of(author.getUserId(), author));
        // 4. 填充 statistic 数据
        ugcService.fillUgcStatistic(ugcInfoList);
        // 5. 过滤不必要的信息
        if (UgcType.ARTICLE == ugcDO.getUgcType()) {
            ugcService.filterNoNeedInfoForListPage(ugcInfoList);
        }
        return ugcInfoList;
    }

    public void queryByUgcId(UgcDO ugcDO) {
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkNotNull(ugcDocument);
        checkEditingOperation(ugcDO, ugcDocument);
        ugcDO.fillWithUgcDocument(ugcDocument);
        // 填充作者信息
        UserDO author = userService.queryByUserId(ugcDocument.getAuthorId());
        userService.fillUserInteractInfo(author);
        userService.fillRelationshipInfo(author);
        ugcDO.setAuthor(author);
        // 填充 Statistic 数据
        ugcService.fillUgcStatistic(Collections.singletonList(ugcDO));
        ugcService.fillUgcInteractInfo(Collections.singletonList(ugcDO), userService.getCurrentUserInfo());
        // 视情况增加 viewCount
        ugcService.incrUgcViewCount(ugcDO, userService.getCurrentUserInfo());
    }

    public List<UgcDO> listTimelineUgcFeed(UgcDO ugcDO) {
        // 1. 游标查询
        List<UgcDocument> ugcDocumentList = ugcService.listTimelineUgcFeed(ugcDO);
        // 2. 封装信息
        return polishUgcInfos(ugcDocumentList);
    }

    public List<UgcDO> queryUserUgc(UgcDO ugcDO) {
        // 1. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.queryUserUgc(ugcDO);
        // 2. 查询作者信息
        String authorId = ugcDO.getAuthorId();
        UserDO userDO = userService.queryByUserId(authorId);
        // 3. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndCursorInfo(ugcDocumentList, Map.of(authorId, userDO));
        // 4. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 5. 文章搜索过滤不必要的信息
        if (UgcType.ARTICLE == ugcDO.getUgcType()) {
            ugcService.filterNoNeedInfoForListPage(ugcDOList);
        }
        return ugcDOList;
    }

    public void interact(UgcDO ugcDO) {
        UserDO currentUser = userService.getCurrentUserInfo();
        UgcInteractionType interactionType = ugcDO.getInteractionType();
        if (interactionType == UgcInteractionType.LIKE) {
            handleLikeUgcInteraction(ugcDO, currentUser);
            return;
        }

        if (interactionType == UgcInteractionType.COLLECT) {
            handleCollectUgcInteraction(ugcDO, currentUser);
        }
    }

    public List<UgcDO> listFollowUgcFeed(UgcDO ugcDO) {
        // 1. 查询关注用户ID
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        Set<String> followUserIds = userService.queryFollowingUserIdsFromCache(currentUserInfo);
        // 2. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.listFollowUgcFeed(ugcDO, followUserIds);
        return polishUgcInfos(ugcDocumentList);
    }

    public List<UgcDO> listRecommendUgcFeed(UgcDO ugcDO) {
        // 1. 查询当前用户感兴趣的标签信息
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        List<String> recommendTags = ugcService.getRecommendTags(currentUserInfo);
        ugcDO.setTags(recommendTags);
        // 2. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.listRecommendUgcFeed(ugcDO);
        // 3. 封装信息
        return polishUgcInfos(ugcDocumentList);
    }

    public List<UgcDO> listHotUgc(UgcDO ugcDO) {
        // 1. 查询热门UGC信息
        List<HotUgcCacheInfo> cacheInfos = ugcService.queryHotUgcFromCache(ugcDO);
        // 2. 填充信息
        return ugcService.fillHotUgc(cacheInfos);
    }

    public List<UgcDO> listQuestions(UgcDO ugcDO) {
        // 1. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.listQuestions(ugcDO);
        // 2. 封装信息
        return polishUgcInfos(ugcDocumentList);
    }

    public List<UgcDO> listSelfCollectedUgc(UgcDO ugcDO) {
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        // 查询，内部设置了下一次查询的 cursor 信息
        List<UgcDocument> ugcDocumentList = ugcService.listSelfCollectedUgc(ugcDO, currentUserInfo);
        return polishUgcInfos(ugcDocumentList, ugcDO);
    }

    public SseEmitter generateSummary(UgcDO ugcDO) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        UgcExtraData extraData = Optional.ofNullable(ugcDocument.getExtraData()).orElseGet(UgcExtraData::new);
        if (StringUtils.isNotBlank(extraData.getUgcSummary())) {
            sendSummaryDirectly(sseEmitter, extraData.getUgcSummary());
            return sseEmitter;
        }
        ugcDO.fillWithUgcDocument(ugcDocument);
        ugcService.aiGenerateSummary(ugcDO, sseEmitter);
        return sseEmitter;
    }

    private void handleLikeUgcInteraction(UgcDO ugcDO, UserDO currentUser) {
        // 喜欢
        if (Boolean.TRUE.equals(ugcDO.getInteractFlag())) {
            doLikeUgc(ugcDO, currentUser);
            return;
        }
        // 取消喜欢
        doCancelLikeUgc(ugcDO, currentUser);
    }

    private List<UgcDO> polishUgcInfos(List<UgcDocument> ugcDocumentList) {
        if (CollectionUtils.isEmpty(ugcDocumentList)) {
            return Collections.emptyList();
        }
        // 3. 批量查询作者信息
        Set<String> authorIds = ugcDocumentList.stream().map(UgcDocument::getAuthorId).collect(Collectors.toSet());
        Map<String, UserDO> id2UserInfoMap = userService.queryBatchByUserId(authorIds);
        // 4. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndCursorInfo(ugcDocumentList, id2UserInfoMap);
        // 5. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 6. 过滤不必要的信息
        ugcService.filterNoNeedInfoForListPage(ugcDOList);
        // 7. 填充交互信息
        ugcService.fillUgcInteractInfo(ugcDOList, userService.getCurrentUserInfo());
        return ugcDOList;
    }

    public List<UgcDO> polishUgcInfos(List<UgcDocument> ugcDocumentList, UgcDO timeCursorCtx) {
        if (CollectionUtils.isEmpty(ugcDocumentList)) {
            return Collections.emptyList();
        }
        // 3. 批量查询作者信息
        Set<String> authorIds = ugcDocumentList.stream().map(UgcDocument::getAuthorId).collect(Collectors.toSet());
        Map<String, UserDO> id2UserInfoMap = userService.queryBatchByUserId(authorIds);
        // 4. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndTimeCursor(ugcDocumentList, id2UserInfoMap, timeCursorCtx.getTimeCursor());
        // 5. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 6. 过滤不必要的信息
        ugcService.filterNoNeedInfoForListPage(ugcDOList);
        // 7. 填充交互信息
        ugcService.fillUgcInteractInfo(ugcDOList, userService.getCurrentUserInfo());
        return ugcDOList;
    }

    private void handleCollectUgcInteraction(UgcDO ugcDO, UserDO currentUser) {
        if (Boolean.TRUE.equals(ugcDO.getInteractFlag())) {
            doCollectUgc(ugcDO, currentUser);
            return;
        }
        doCancelCollectUgc(ugcDO, currentUser);
    }

    private void doLikeUgc(UgcDO ugcDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasLikeOptional = Optional.ofNullable(ugcRelationshipRepository.queryLikeRelationship(ugcDO.getUgcId(), currentUser.getUserId()));
        if (hasLikeOptional.isPresent()) {
            return;
        }
        // 创建用户节点信息
        userService.createUserIfNeed(currentUser);
        // 添加喜欢关系，增加点赞数
        ugcService.likeUgc(ugcDO, currentUser);
        // 发送通知给作者
        notificationManager.sendUgcLikeNotification(currentUser, ugcDO.getUgcId());
    }

    private void doCancelLikeUgc(UgcDO ugcDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasLikeOptional = Optional.ofNullable(ugcRelationshipRepository.queryLikeRelationship(ugcDO.getUgcId(), currentUser.getUserId()));
        if (hasLikeOptional.isEmpty()) {
            return;
        }
        ugcService.cancelLikeUgc(ugcDO, currentUser);
    }

    private void doCollectUgc(UgcDO ugcDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasCollectOptional = Optional.ofNullable(ugcRelationshipRepository.queryCollectRelationship(ugcDO.getUgcId(), currentUser.getUserId()));
        if (hasCollectOptional.isPresent()) {
            return;
        }
        // 创建用户节点信息
        userService.createUserIfNeed(currentUser);
        // 添加收藏关系
        ugcService.collectUgc(ugcDO, currentUser);
        // 发送通知给作者
        notificationManager.sendUgcCollectNotification(currentUser, ugcDO.getUgcId());
    }

    private void doCancelCollectUgc(UgcDO ugcDO, UserDO currentUser) {
        // 幂等校验
        Optional<UgcInteractRelationship> hasLikeOptional = Optional.ofNullable(ugcRelationshipRepository.queryCollectRelationship(ugcDO.getUgcId(), currentUser.getUserId()));
        if (hasLikeOptional.isEmpty()) {
            return;
        }
        ugcService.cancelCollectUgc(ugcDO, currentUser);
    }

    private void fillCurrUserAsAuthor(UgcDO ugcDO) {
        UserDO author = userService.getCurrentUserInfo();
        ugcDO.setAuthor(author);
        ugcDO.setAuthorId(author.getUserId());
    }

    private void fillUgcInfo(UgcDO ugcDO) {
        String categoryId = ugcDO.getCategoryId();
        if (StringUtils.isNotBlank(categoryId)) {
            UgcCategoryPO ugcCategoryPO = ugcCategoryRepository.queryByCategoryId(categoryId);
            ugcDO.setCategoryName(ugcCategoryPO.getCategoryName());
        }
        if (UgcType.QUESTION == ugcDO.getUgcType()) {
            // 生成摘要
            String plainContent = UgcContentUtil.markdownToPlainText(ugcDO.getContent());
            ugcDO.setSummary(plainContent);
        }
    }

    private void checkSelfAuthor(UgcDO ugcDO, UgcDocument ugcDocument) {
        String authorId = ugcDocument.getAuthorId();
        UserDO author = ugcDO.getAuthor();
        if (authorId.equals(author.getUserId())) {
            return;
        }
        throw AppBizException.of(OPERATION_DENIED, "无权修改！");
    }
    private void checkEditingOperation(UgcDO ugcDO, UgcDocument ugcDocument) {
        if (Boolean.TRUE.equals(ugcDO.getEditing())) {
            fillCurrUserAsAuthor(ugcDO);
            checkSelfAuthor(ugcDO, ugcDocument);
        }
    }

    private void sendSummaryDirectly(SseEmitter sseEmitter, String summary) {
        // 将 summary 按行拆分
        Flowable<String> lineFlowable = Flowable.fromIterable(Arrays.asList(summary.split(SymbolConstant.NEW_LINE)));

        lineFlowable
            .observeOn(Schedulers.io())
            .doOnNext(line -> {
                String formattedLine = line + SymbolConstant.NEW_LINE;
                sseEmitter.send(formattedLine);
            })
            .doOnError(sseEmitter::completeWithError)
            .doOnComplete(sseEmitter::complete)
            .delay(200, TimeUnit.MILLISECONDS)
            .subscribe();
    }

}
