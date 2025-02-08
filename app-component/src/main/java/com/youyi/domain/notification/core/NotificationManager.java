package com.youyi.domain.notification.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationExtraData;
import com.youyi.domain.notification.repository.NotificationRepository;
import com.youyi.domain.ugc.repository.CommentaryRepository;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.CommentaryDocument;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.domain.user.model.UserDO;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.conf.ConfigKey.NOTIFICATION_SUMMARY_TEMPLATE;
import static com.youyi.common.type.notification.NotificationType.UGC_COMMENT;
import static com.youyi.common.type.notification.NotificationType.UGC_COMMENT_REPLY;
import static com.youyi.common.type.notification.NotificationType.UGC_LIKE;
import static com.youyi.common.type.notification.NotificationType.USER_FOLLOW;
import static com.youyi.infra.conf.core.SystemConfigService.checkConfig;
import static com.youyi.infra.conf.core.SystemConfigService.getCacheValue;
import static com.youyi.infra.conf.core.SystemConfigService.getMapConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Component
@RequiredArgsConstructor
public class NotificationManager implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationManager.class);
    private static ThreadPoolExecutor notificationExecutor;

    private final NotificationRepository notificationRepository;
    private final UgcRepository ugcRepository;
    private final CommentaryRepository commentaryRepository;

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
            notificationTpeConfig.getThreadFactory(LOGGER),
            notificationTpeConfig.getRejectedHandler()
        );
    }

    /**
     * 发送关注通知
     */
    public void sendUserFollowNotification(UserDO sender, UserDO receiver) {
        sendNotificationAsync(
            sender,
            receiver.getUserId(),
            USER_FOLLOW,
            receiver.getUserId(),
            NotificationTemplateConfig.generateSummary(USER_FOLLOW, sender.getNickName()),
            SymbolConstant.EMPTY
        );
    }

    /**
     * 发送 UGC 点赞通知
     */
    public void sendUgcLikeNotification(UserDO sender, String ugcId) {
        notificationExecutor.execute(() -> {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotificationAsync(
                sender,
                ugcDocument.getAuthorId(),
                UGC_LIKE,
                ugcId,
                NotificationTemplateConfig.generateSummary(UGC_LIKE, sender.getNickName(), UgcType.of(ugcDocument.getType()).getDesc(), ugcDocument.getTitle()),
                SymbolConstant.EMPTY
            );
        });
    }

    /**
     * 发送 UGC 评论通知
     */
    public void sendUgcCommentNotification(UserDO sender, String ugcId, String content) {
        notificationExecutor.execute(() -> {
            UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
            sendNotificationAsync(
                sender,
                ugcDocument.getAuthorId(),
                UGC_COMMENT,
                ugcId,
                NotificationTemplateConfig.generateSummary(UGC_COMMENT, sender.getNickName(), UgcType.of(ugcDocument.getType()).getDesc(), ugcDocument.getTitle()),
                content
            );
        });
    }

    /**
     * 发送 UGC 评论回复通知
     */
    public void sendUgcCommentReplyNotification(UserDO sender, String commentaryId, String content) {
        notificationExecutor.execute(() -> {
            CommentaryDocument commentaryDocument = commentaryRepository.queryByCommentaryId(commentaryId);
            sendNotificationAsync(
                sender,
                commentaryDocument.getCommentatorId(),
                UGC_COMMENT_REPLY,
                commentaryId,
                NotificationTemplateConfig.generateSummary(UGC_COMMENT_REPLY, sender.getNickName()),
                content
            );
        });
    }

    private void sendNotificationAsync(UserDO sender, String receiverId, NotificationType type, String targetId, String summary, String content) {
        if (!needNotify(sender.getUserId(), receiverId)) {
            return;
        }

        try {
            NotificationExtraData extraData = new NotificationExtraData();
            extraData.setTargetId(targetId);
            extraData.setContent(content);
            extraData.setSummary(summary);

            NotificationDO notification = new NotificationDO();
            notification.setSender(UserDO.of(sender.getUserId()));
            notification.setReceiver(UserDO.of(receiverId));
            notification.setNotificationType(type);
            notification.setExtraData(extraData);
            notification.create();

            notificationRepository.insert(notification.buildToSaveNotificationPO());
        } catch (Exception e) {
            LOGGER.error("Failed to send notification of type {}", type, e);
        }
    }

    private boolean needNotify(String senderId, String receiverId) {
        return !senderId.equals(receiverId);
    }

    static class NotificationTemplateConfig {
        private static final String DEFAULT_KEY = "DEFAULT";

        public static String generateSummary(NotificationType type, Object... args) {
            Map<String, String> templateMapping = getMapConfig(NOTIFICATION_SUMMARY_TEMPLATE, String.class, String.class);
            String template = templateMapping.getOrDefault(type.name(), templateMapping.get(DEFAULT_KEY));
            return MessageFormat.format(template, args);
        }
    }
}

