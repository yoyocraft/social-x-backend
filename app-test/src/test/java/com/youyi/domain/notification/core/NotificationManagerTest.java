package com.youyi.domain.notification.core;

import com.youyi.BaseIntegrationTest;
import com.youyi.domain.user.model.UserDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/02
 */
class NotificationManagerTest extends BaseIntegrationTest {

    @Autowired
    NotificationManager notificationManager;

    @Test
    void testSendUgcCommentNotification() throws InterruptedException {
        UserDO sender = UserDO.of("1883827647466573824");
        sender.setNickname("游艺Geek");
        Assertions.assertDoesNotThrow(() -> {
            notificationManager.sendUgcCommentNotification(
                sender,
                "1895394725898113024",
                "测试通知系统",
                "1895394388223074304"
            );
        });

        Thread.sleep(2000);
    }

    @Test
    void testSendUgcLikeNotification() throws InterruptedException {
        UserDO sender = UserDO.of("1883829503093772288");
        sender.setNickname("social_x_84910306");
        Assertions.assertDoesNotThrow(() -> {
            notificationManager.sendUgcLikeNotification(
                sender,
                "1895394388344709120"
            );
        });

        Thread.sleep(2000);
    }

    @Test
    void testSendUgcCollectNotification() throws InterruptedException {
        UserDO sender = UserDO.of("1883829503093772288");
        sender.setNickname("social_x_84910306");
        Assertions.assertDoesNotThrow(() -> {
            notificationManager.sendUgcCollectNotification(
                sender,
                "1895394388344709120"
            );
        });

        Thread.sleep(2000);
    }

    @Test
    void testSendUserFollowNotification() throws InterruptedException {
        UserDO sender = UserDO.of("1883829503093772288");
        sender.setNickname("social_x_84910306");
        Assertions.assertDoesNotThrow(() -> {
            notificationManager.sendUserFollowNotification(
                sender,
                UserDO.of("1883827647466573824")
            );
        });
        Thread.sleep(1500);
    }
}
