package com.youyi.domain.notification.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.domain.ugc.constant.UgcConstant;
import com.youyi.domain.ugc.type.UgcType;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationExtraData;
import com.youyi.domain.notification.repository.NotificationRepository;
import com.youyi.domain.notification.type.NotificationTemplateKey;
import com.youyi.domain.notification.type.NotificationType;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import com.youyi.infra.conf.core.ConfigKey;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.SystemConstant.DEFAULT_KEY;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_ADOPT;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_COLLECT;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_COMMENT;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_COMMENT_REPLY;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_FEATURED;
import static com.youyi.domain.notification.type.NotificationTemplateKey.UGC_LIKE;
import static com.youyi.domain.notification.type.NotificationTemplateKey.USER_FOLLOW;
import static com.youyi.infra.conf.core.Conf.checkConfig;
import static com.youyi.infra.conf.core.Conf.getCacheValue;
import static com.youyi.infra.conf.core.Conf.getMapConfig;
import static com.youyi.infra.conf.core.ConfigKey.NOTIFICATION_SUMMARY_TEMPLATE;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Component
@RequiredArgsConstructor
public class NotificationManager implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);
    private static ThreadPoolExecutor notificationExecutor;

    private final UgcRepository ugcRepository;
    private final CommentaryRepository commentaryRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(ConfigKey.NOTIFICATION_THREAD_POOL_CONFIG);
        initAsyncExecutor();
    }

    private void initAsyncExecutor() {
        ThreadPoolConfigWrapper notificationTpeConfig = getCacheValue(ConfigKey.NOTIFICATION_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        notificationExecutor = new ThreadPoolExecutor(
            notificationTpeConfig.getCorePoolSize(),
            notificationTpeConfig.getMaximumPoolSize(),
            notificationTpeConfig.getKeepAliveTime(),
            notificationTpeConfig.getTimeUnit(),
            notificationTpeConfig.getQueue(),
            notificationTpeConfig.getThreadFactory(logger),
            notificationTpeConfig.getRejectedHandler()
        );
    }

    /**
     * 发送关注通知
     */
    public void sendUserFollowNotification(UserDO sender, UserDO receiver) {
        sendNotification(
            sender,
            receiver.getUserId(),
            NotificationType.FOLLOW,
            receiver.getUserId(),
            generateSummary(USER_FOLLOW, sender.getNickname()),
            SymbolConstant.EMPTY
        );
    }

    /**
     * 发送 UGC 点赞通知
     */
    public void sendUgcLikeNotification(UserDO sender, String ugcId) {
        notificationExecutor.execute(() -> {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotification(
                sender,
                ugcDocument.getAuthorId(),
                NotificationType.INTERACT,
                ugcId,
                ugcDocument.getType(),
                generateSummary(UGC_LIKE, UgcType.of(ugcDocument.getType()).getDesc(), ugcDocument.getTitle()),
                SymbolConstant.EMPTY
            );
        });
    }

    /**
     * 发送 UGC 收藏通知
     */
    public void sendUgcCollectNotification(UserDO sender, String ugcId) {
        notificationExecutor.execute(() -> {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotification(
                sender,
                ugcDocument.getAuthorId(),
                NotificationType.INTERACT,
                ugcId,
                ugcDocument.getType(),
                generateSummary(UGC_COLLECT, UgcType.of(ugcDocument.getType()).getDesc(), ugcDocument.getTitle()),
                SymbolConstant.EMPTY
            );
        });
    }

    /**
     * 发送 UGC 采纳通知
     */
    public void sendUgcAdoptNotification(UserDO sender, String commentaryId, UgcDocument ugcDocument) {
        notificationExecutor.execute(() -> {
            CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryId);
            sendNotification(
                sender,
                commentaryDocument.getCommentatorId(),
                NotificationType.INTERACT,
                ugcDocument.getUgcId(),
                ugcDocument.getType(),
                commentaryId,
                UgcConstant.COMMENTARY_ID,
                generateSummary(UGC_ADOPT),
                commentaryDocument.getCommentary()
            );
        });
    }

    /**
     * 发送 UGC 精选通知
     */
    public void sendUgcFeaturedNotification(UserDO sender, String commentaryId, UgcDocument ugcDocument) {
        notificationExecutor.execute(() -> {
            CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryId);
            sendNotification(
                sender,
                commentaryDocument.getCommentatorId(),
                NotificationType.INTERACT,
                ugcDocument.getUgcId(),
                ugcDocument.getType(),
                commentaryId,
                UgcConstant.COMMENTARY_ID,
                generateSummary(UGC_FEATURED),
                commentaryDocument.getCommentary()
            );
        });
    }

    /**
     * 发送 UGC 评论通知
     */
    public void sendUgcCommentNotification(UserDO sender, String commentaryId, String content, String ugcId) {
        notificationExecutor.execute(() -> {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotification(
                sender,
                ugcDocument.getAuthorId(),
                NotificationType.COMMENT,
                ugcId,
                ugcDocument.getType(),
                commentaryId,
                UgcConstant.COMMENTARY_ID,
                generateSummary(UGC_COMMENT, UgcType.of(ugcDocument.getType()).getDesc(), ugcDocument.getTitle()),
                content
            );
        });
    }

    /**
     * 发送 UGC 评论回复通知
     */
    public void sendUgcCommentReplyNotification(UserDO sender, String commentaryId, String content, String ugcId) {
        notificationExecutor.execute(() -> {
            CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryId);
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotification(
                sender,
                commentaryDocument.getCommentatorId(),
                NotificationType.COMMENT,
                ugcId,
                ugcDocument.getType(),
                generateSummary(UGC_COMMENT_REPLY, sender.getNickname()),
                content
            );
        });
    }

    public void sendSystemNotification(UserDO sender, String title, String content) {
        notificationExecutor.execute(() -> sendNotification(
            sender,
            SymbolConstant.EMPTY,
            NotificationType.SYSTEM,
            SymbolConstant.EMPTY,
            title,
            content
        ));
    }

    public void sendNotification(UserDO sender, String receiverId, NotificationType type, String targetId, String summary, String content) {
        sendNotification(sender, receiverId, type, targetId, SymbolConstant.EMPTY, summary, content);
    }

    public void sendNotification(
        UserDO sender,
        String receiverId,
        NotificationType type,
        String targetId,
        String targetType,
        String summary,
        String content
    ) {
        sendNotification(
            sender,
            receiverId,
            type,
            targetId,
            targetType,
            SymbolConstant.EMPTY,
            SymbolConstant.EMPTY,
            summary,
            content
        );
    }

    public void sendNotification(
        UserDO sender,
        String receiverId,
        NotificationType type,
        String targetId,
        String targetType,
        String relatedId,
        String relatedType,
        String summary,
        String content
    ) {
        if (!needNotify(sender.getUserId(), receiverId)) {
            logger.info("{} do not need to receive notification of type {}", receiverId, type);
            return;
        }

        try {
            NotificationExtraData extraData = new NotificationExtraData();
            extraData.setTargetId(targetId);
            extraData.setContent(content);
            extraData.setSummary(summary);
            extraData.setTargetType(targetType);
            extraData.setRelatedId(relatedId);
            extraData.setRelatedType(relatedType);

            NotificationDO notification = new NotificationDO();
            notification.setSender(sender);
            notification.setReceiver(UserDO.of(receiverId));
            notification.setNotificationType(type);
            notification.setExtraData(extraData);
            notification.create();

            notificationRepository.insert(notification.buildToSaveNotificationPO());
        } catch (Exception e) {
            logger.error("Failed to send notification of type {}", type, e);
        }
    }

    private boolean needNotify(String senderId, String receiverId) {
        return StringUtils.isNotBlank(senderId) && !senderId.equals(receiverId);
    }

    private String generateSummary(NotificationTemplateKey type, Object... args) {
        Map<String, String> templateMapping = getMapConfig(NOTIFICATION_SUMMARY_TEMPLATE, String.class, String.class);
        String template = templateMapping.getOrDefault(type.name(), templateMapping.get(DEFAULT_KEY));
        return MessageFormat.format(template, args);
    }
}

