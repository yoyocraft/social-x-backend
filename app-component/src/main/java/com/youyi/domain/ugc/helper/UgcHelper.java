package com.youyi.domain.ugc.helper;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.task.TaskType;
import com.youyi.common.type.ugc.UgcInteractionType;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.notification.core.NotificationManager;
import com.youyi.domain.task.core.SysTaskService;
import com.youyi.domain.ugc.core.UgcService;
import com.youyi.domain.ugc.core.UgcTpeContainer;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.repository.UgcRelationshipRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    private final UgcRelationshipRepository ugcRelationshipRepository;

    public void publishUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        ugcService.publishUgc(ugcDO);
    }

    public void deleteUgc(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkSelfAuthor(ugcDO, ugcDocument);
        ugcRepository.deleteUgc(ugcDO.getUgcId());
        // 异步写入本地 SysTask，异步处理删除后续
        ugcTpeContainer.getUgcSysTaskExecutor().execute(() -> sysTaskService.saveCommonSysTask(ugcDO.getUgcId(), TaskType.UGC_DELETE_EVENT));
    }

    public List<UgcDO> querySelfUgc(UgcDO ugcDO) {
        // 1. 查询作者信息
        fillCurrUserAsAuthorInfo(ugcDO);
        // 2. 查询
        List<UgcDocument> ugcDocuments = ugcService.querySelfUgcWithCursor(ugcDO);
        // 3. 封装作者信息和游标信息
        UserDO author = ugcDO.getAuthor();
        List<UgcDO> ugcInfoList = ugcService.fillAuthorAndCursorInfo(ugcDocuments, ImmutableMap.of(author.getUserId(), author));
        // 4. 填充 statistic 数据
        ugcService.fillUgcStatistic(ugcInfoList);
        // 5. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcInfoList);
        return ugcInfoList;
    }

    public void queryByUgcId(UgcDO ugcDO) {
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkNotNull(ugcDocument);
        ugcDO.fillWithUgcDocument(ugcDocument);
        // 填充作者信息
        UserDO author = userService.queryByUserId(ugcDocument.getAuthorId());
        ugcDO.setAuthor(author);
        // 填充 Statistic 数据
        ugcService.fillUgcStatistic(Collections.singletonList(ugcDO));
        // 视情况增加 viewCount
        ugcService.incrUgcViewCount(ugcDO, userService.getCurrentUserInfo());
    }

    public void updateUgcStatus(UgcDO ugcDO) {
        fillCurrUserAsAuthorInfo(ugcDO);
        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcDO.getUgcId());
        checkNotNull(ugcDocument);
        checkSelfAuthor(ugcDO, ugcDocument);
        checkStatusValidation(ugcDO, ugcDocument);
        ugcRepository.updateUgcStatus(ugcDO.getUgcId(), ugcDO.getStatus().name());
    }

    public List<UgcDO> queryByCursorForMainPage(UgcDO ugcDO) {
        // 1. 游标查询
        List<UgcDocument> ugcDocumentList = ugcService.queryWithUgcIdCursor(ugcDO);
        // 2. 批量查询作者信息
        Set<String> authorIds = ugcDocumentList.stream().map(UgcDocument::getAuthorId).collect(Collectors.toSet());
        Map<String, UserDO> id2UserInfoMap = userService.queryBatchByUserId(authorIds);
        // 3. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndCursorInfo(ugcDocumentList, id2UserInfoMap);
        // 4. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 5. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcDOList);
        return ugcDOList;
    }

    public List<UgcDO> queryByCursorForUserPage(UgcDO ugcDO) {
        // 1. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.queryWithUgcIdAndAuthorIdCursor(ugcDO);
        // 2. 查询作者信息
        String authorId = ugcDO.getAuthorId();
        UserDO userDO = userService.queryByUserId(authorId);
        // 3. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndCursorInfo(ugcDocumentList, ImmutableMap.of(authorId, userDO));
        // 4. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 5. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcDOList);
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

    public List<UgcDO> queryByCursorForFollowPage(UgcDO ugcDO) {
        // 1. 查询关注用户ID
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        Set<String> followUserIds = userService.queryFollowingUserIdsFromCache(currentUserInfo);
        // 2. 游标查询UGC信息
        List<UgcDocument> ugcDocumentList = ugcService.queryByAuthorIdsWithCursor(ugcDO, followUserIds);
        // 3. 批量查询作者信息
        Set<String> authorIds = ugcDocumentList.stream().map(UgcDocument::getAuthorId).collect(Collectors.toSet());
        Map<String, UserDO> id2UserInfoMap = userService.queryBatchByUserId(authorIds);
        // 4. 封装信息
        List<UgcDO> ugcDOList = ugcService.fillAuthorAndCursorInfo(ugcDocumentList, id2UserInfoMap);
        // 5. 填充 Statistic 数据
        ugcService.fillUgcStatistic(ugcDOList);
        // 6. 过滤不必要的信息
        filterNoNeedInfoForListPage(ugcDOList);
        return ugcDOList;
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

    private void fillCurrUserAsAuthorInfo(UgcDO ugcDO) {
        UserDO author = userService.getCurrentUserInfo();
        ugcDO.setAuthor(author);
        ugcDO.setAuthorId(author.getUserId());
    }

    private void checkSelfAuthor(UgcDO ugcDO, UgcDocument ugcDocument) {
        String authorId = ugcDocument.getAuthorId();
        UserDO author = ugcDO.getAuthor();
        if (authorId.equals(author.getUserId())) {
            return;
        }
        throw AppBizException.of(OPERATION_DENIED, "无权修改！");
    }

    private void checkStatusValidation(UgcDO ugcDO, UgcDocument ugcDocument) {
        UgcStatus updateStatus = ugcDO.getStatus();
        String statusFromDB = ugcDocument.getStatus();

        // 设置 PRIVATE 必须是 PUBLISHED
        if (updateStatus == UgcStatus.PRIVATE && !UgcStatus.PUBLISHED.name().equals(statusFromDB)) {
            throw AppBizException.of(OPERATION_DENIED, "非审核通过的稿件无法设置私密！");
        }
        // 设置 PUBLISHED 必须是 PRIVATE
        if (updateStatus == UgcStatus.PUBLISHED && !UgcStatus.PRIVATE.name().equals(statusFromDB)) {
            throw AppBizException.of(OPERATION_DENIED, "非私密稿件无法设置为公开！");
        }
    }

    private void filterNoNeedInfoForListPage(List<UgcDO> ugcDOList) {
        ugcDOList.forEach(ugcDO -> {
            // 列表页无需返回 content
            ugcDO.setContent(null);
        });
    }
}
