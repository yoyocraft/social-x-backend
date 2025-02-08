package com.youyi.domain.notification.core;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationUnreadInfo;
import com.youyi.domain.notification.repository.NotificationRepository;
import com.youyi.domain.notification.repository.po.NotificationPO;
import com.youyi.domain.user.model.UserDO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.notification.NotificationStatus.READ;
import static com.youyi.common.type.notification.NotificationStatus.UNREAD;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/06
 */
@Component
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationPO> querySelfByTypeWithCursor(NotificationDO notificationDO) {
        return notificationRepository.querySelfByTypeWithCursor(
            notificationDO.getNotificationType().name(),
            notificationDO.getReceiver().getUserId(),
            notificationDO.getCursor(),
            notificationDO.getSize()
        );
    }

    public List<NotificationUnreadInfo> queryUnreadCountGroupByType(NotificationDO notificationDO) {
        return notificationRepository.queryUnreadCountGroupByType(notificationDO.getReceiver().getUserId());
    }

    public NotificationUnreadInfo queryTotalUnreadCount(NotificationDO notificationDO) {
        Long totalCount = notificationRepository.queryUnreadCount(notificationDO.getReceiver().getUserId());
        return NotificationUnreadInfo.of(NotificationType.ALL, totalCount);
    }

    public List<NotificationDO> fillUserAndCursorInfo(List<NotificationPO> poList, Map<String, UserDO> senderId2InfoMap, UserDO receiver) {
        final String nextCursor = Optional.ofNullable(
            CollectionUtils.isEmpty(poList)
                ? null
                : poList.get(poList.size() - 1).getNotificationId()
        ).orElse(SymbolConstant.EMPTY);
        return poList.stream()
            .map(po -> {
                NotificationDO info = new NotificationDO();
                info.fillWithPO(po);
                info.setCursor(nextCursor);
                info.setReceiver(receiver);
                info.setSender(senderId2InfoMap.get(po.getSenderId()));
                return info;
            })
            .collect(Collectors.toList());
    }

    public void readSingleNotification(NotificationDO notificationDO) {
        notificationRepository.updateStatusByNotificationIdWithReadTime(
            notificationDO.getNotificationId(),
            notificationDO.getReceiver().getUserId(),
            READ.getCode(),
            UNREAD.getCode(),
            System.currentTimeMillis()
        );
    }

    public void readAllNotificationByType(NotificationDO notificationDO) {
        notificationRepository.updateStatusByTypeAndReceiverIdWithReadTime(
            notificationDO.getNotificationType().name(),
            notificationDO.getReceiver().getUserId(),
            READ.getCode(),
            UNREAD.getCode(),
            System.currentTimeMillis()
        );
    }

    public void readAll(NotificationDO notificationDO) {
        notificationRepository.updateStatusByReceiverIdWithReadTime(
            notificationDO.getReceiver().getUserId(),
            READ.getCode(),
            UNREAD.getCode(),
            System.currentTimeMillis()
        );
    }

    public List<NotificationDO> fillUnreadInfo(List<NotificationUnreadInfo> unreadInfos) {
        return unreadInfos.stream()
            .map(unreadInfo -> {
                NotificationDO info = new NotificationDO();
                info.setNotificationType(NotificationType.of(unreadInfo.getNotificationType()));
                info.setUnreadCount(unreadInfo.getUnreadCount());
                return info;
            })
            .collect(Collectors.toList());
    }
}
