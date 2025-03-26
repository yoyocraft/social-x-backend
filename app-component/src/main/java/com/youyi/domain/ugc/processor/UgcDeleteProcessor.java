package com.youyi.domain.ugc.processor;

import com.google.common.collect.Lists;
import com.youyi.common.constant.RepositoryConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.task.core.SysTaskService;
import com.youyi.domain.task.core.TaskProcessor;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.task.type.TaskType;
import com.youyi.domain.ugc.core.CommentaryService;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.repository.UgcRelationshipRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.infra.cache.CacheRepository;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcCollectCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcCommentaryCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcLikeCountKey;
import static com.youyi.infra.cache.key.UgcCacheKeyRepo.ofUgcViewCountKey;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Component
@RequiredArgsConstructor
public class UgcDeleteProcessor implements TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UgcDeleteProcessor.class);

    private final SysTaskService sysTaskService;
    private final CommentaryService commentaryService;
    private final UgcRelationshipRepository ugcRelationshipRepository;
    private final CacheRepository cacheRepository;

    @Override
    public void process(String taskId, SysTaskExtraData extraData) {
        if (Objects.isNull(extraData)) {
            logger.warn("extraData is null, taskId: {}", taskId);
            return;
        }
        String ugcId = extraData.getTargetId();
        logger.info("[TaskProcessor]delete ugc, taskId: {}, extraData: {}", taskId, GsonUtil.toJson(extraData));

        // 删除对应的点赞关系
        ugcRelationshipRepository.deleteAllLikeRelationships(ugcId);
        // 删除对应的收藏关系
        ugcRelationshipRepository.deleteAllCollectRelationships(ugcId);
        // 删除 UGC 节点
        logger.info("delete ugc node, ugcId: {}", ugcId);
        ugcRelationshipRepository.deleteUgcNode(ugcId);
        // 删除所有的缓存数据
        deleteCacheData(ugcId);
        // 删除对应的评论，写入删除事件，后续依赖 TaskTrigger 处理
        deleteRelatedCommentary(ugcId);
    }

    private void deleteRelatedCommentary(String ugcId) {
        logger.info("delete related commentary, ugcId: {}", ugcId);
        // 删除评论
        CommentaryDO commentaryDO = new CommentaryDO();
        commentaryDO.setCursor(RepositoryConstant.INIT_QUERY_CURSOR);
        commentaryDO.setUgcId(ugcId);
        commentaryDO.setSize(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE));
        List<String> allToDeleteCommentaryIds = Lists.newArrayList();
        while (true) {
            // 删除根评论即可，不需要递归查询子评论，在 CommentaryDeleteProcessor 中会处理对应的子评论
            List<CommentaryDocument> commentaryDocuments = commentaryService.listRootCommentary(commentaryDO);
            if (CollectionUtils.isEmpty(commentaryDocuments)) {
                break;
            }
            List<String> toDeleteCommentaryIds = commentaryDocuments.stream().map(CommentaryDocument::getCommentaryId).toList();
            allToDeleteCommentaryIds.addAll(toDeleteCommentaryIds);
            String nextCursor = commentaryDocuments.get(commentaryDocuments.size() - 1).getCommentaryId();
            commentaryDO.setCursor(nextCursor);
        }
        logger.info("delete related commentary, ugcId: {}, allToDeleteCommentaryIds: {}", ugcId, allToDeleteCommentaryIds);
        sysTaskService.saveBatchCommonSysTask(allToDeleteCommentaryIds, TaskType.COMMENTARY_DELETE_EVENT);
    }

    private void deleteCacheData(String ugcId) {
        List<String> deletedKeys = List.of(
            ofUgcViewCountKey(ugcId),
            ofUgcLikeCountKey(ugcId),
            ofUgcCollectCountKey(ugcId),
            ofUgcCommentaryCountKey(ugcId)
        );
        cacheRepository.batchDelete(deletedKeys);
    }
}
