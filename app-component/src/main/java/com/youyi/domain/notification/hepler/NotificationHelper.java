package com.youyi.domain.notification.hepler;

import com.youyi.domain.notification.core.NotificationManager;
import com.youyi.domain.notification.core.NotificationService;
import com.youyi.domain.notification.model.NotificationDO;
import com.youyi.domain.notification.model.NotificationUnreadInfo;
import com.youyi.domain.notification.repository.po.NotificationPO;
import com.youyi.domain.user.core.UserService;
import com.youyi.domain.user.model.UserDO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/06
 */
@Service
@RequiredArgsConstructor
public class NotificationHelper {

    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationManager notificationManager;

    public List<NotificationDO> queryUnreadCount(NotificationDO notificationDO) {
        // 0. 填充当前用户为接收者
        fillCurrUserAsReceiver(notificationDO);

        // 1. 查询出未读数量
        List<NotificationUnreadInfo> unreadInfos;
        boolean queryAll = Boolean.TRUE.equals(notificationDO.getQueryAll());
        if (queryAll) {
            // 查询总数
            unreadInfos = Collections.singletonList(notificationService.queryTotalUnreadCount(notificationDO));
        } else {
            // 查询每个类型的未读数量
            unreadInfos = notificationService.queryUnreadCountGroupByType(notificationDO);
        }

        if (CollectionUtils.isEmpty(unreadInfos)) {
            return new ArrayList<>();
        }

        // 2. 填充数据并返回
        return notificationService.fillUnreadInfo(unreadInfos);
    }

    public List<NotificationDO> queryByTypeWithCursor(NotificationDO notificationDO) {
        // 0. 填充当前用户为接收者
        fillCurrUserAsReceiver(notificationDO);
        // 1. 查询出通知列表
        List<NotificationPO> poList = notificationService.querySelfByTypeWithCursor(notificationDO);
        if (CollectionUtils.isEmpty(poList)) {
            return new ArrayList<>();
        }

        // 2. 批量查询出发送者信息
        Set<String> senderIds = poList.stream()
            .map(NotificationPO::getSenderId)
            .collect(Collectors.toSet());
        Map<String, UserDO> senderId2InfoMap = userService.queryBatchByUserId(senderIds);

        // 3. 填充数据并返回
        UserDO receiver = notificationDO.getReceiver();
        List<NotificationDO> notificationDOList = notificationService.fillUserAndCursorInfo(poList, senderId2InfoMap, receiver);
        notificationService.fillUserFollowedInfo(notificationDOList, receiver);
        return notificationDOList;
    }

    public void readSingleNotification(NotificationDO notificationDO) {
        fillCurrUserAsReceiver(notificationDO);
        notificationService.readSingleNotification(notificationDO);
    }

    public void readAllNotificationByType(NotificationDO notificationDO) {
        fillCurrUserAsReceiver(notificationDO);
        notificationService.readAllNotificationByType(notificationDO);
    }

    public void readAll(NotificationDO notificationDO) {
        fillCurrUserAsReceiver(notificationDO);
        notificationService.readAll(notificationDO);
    }

    private void fillCurrUserAsReceiver(NotificationDO notificationDO) {
        UserDO currentUserInfo = userService.getCurrentUserInfo();
        notificationDO.setReceiver(currentUserInfo);
    }

    public void publish(NotificationDO notificationDO) {
        UserDO sender = userService.getCurrentUserInfo();
        notificationManager.sendSystemNotification(sender, notificationDO.getTitle(), notificationDO.getContent());
    }
}
