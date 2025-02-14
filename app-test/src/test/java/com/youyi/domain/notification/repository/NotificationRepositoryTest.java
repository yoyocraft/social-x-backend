package com.youyi.domain.notification.repository;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.notification.NotificationStatus;
import com.youyi.common.type.notification.NotificationType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.notification.model.NotificationUnreadInfo;
import com.youyi.domain.notification.repository.po.NotificationPO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
class NotificationRepositoryTest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRepositoryTest.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void testInsert() {
        notificationRepository.insert(buildPO());
    }

    @Test
    void testQueryUnreadCountByType() {
        String receiverId = "1883827647466573824";
        List<NotificationUnreadInfo> unreadInfoList = notificationRepository.queryUnreadCountGroupByType(receiverId);
        logger.info("unreadInfoList: {}", GsonUtil.toJson(unreadInfoList));
    }

    NotificationPO buildPO() {
        NotificationPO po = new NotificationPO();
        po.setNotificationId(IdSeqUtil.genNotificationId());
        po.setNotificationType(NotificationType.UGC_LIKE.name());
        po.setNotificationStatus(NotificationStatus.UNREAD.getCode());
        po.setReceiverId("1883827647466573824");
        po.setSenderId("1883829503093772288");
        return po;
    }
}
