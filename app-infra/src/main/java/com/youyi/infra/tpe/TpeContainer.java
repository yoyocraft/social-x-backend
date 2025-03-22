package com.youyi.infra.tpe;

import com.youyi.infra.conf.util.ThreadPoolExecutorUtil;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.infra.conf.core.ConfigKey.AUDIT_UGC_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.NOTIFICATION_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.RECORD_OP_LOG_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.UGC_STATISTICS_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.UGC_SYS_TASK_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.USER_COMMON_OP_THREAD_POOL_CONFIG;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/22
 */
@Getter
@Component
@RequiredArgsConstructor
public class TpeContainer implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(TpeContainer.class);

    private ThreadPoolExecutor auditUgcExecutor;
    private ThreadPoolExecutor ugcStatisticsExecutor;
    private ThreadPoolExecutor ugcSysTaskExecutor;

    private ThreadPoolExecutor notificationExecutor;

    private ThreadPoolExecutor recordOpLogExecutor;

    private ThreadPoolExecutor userCommonOpExecutor;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        initAsyncExecutor();
    }

    @Override
    public void destroy() {
        destroyAsyncExecutor();
    }

    private void initAsyncExecutor() {
        initSystemExecutor();
        initUgcExecutor();
        initNotificationExecutor();
        initUserExecutor();
    }

    private void destroyAsyncExecutor() {
        destroyUgcExecutor();
        destroyNotificationExecutor();
        destroySystemExecutor();
        destroyUserExecutor();
    }

    private void initUgcExecutor() {
        auditUgcExecutor = ThreadPoolExecutorUtil.createExecutor(AUDIT_UGC_THREAD_POOL_CONFIG, logger);
        ugcStatisticsExecutor = ThreadPoolExecutorUtil.createExecutor(UGC_STATISTICS_THREAD_POOL_CONFIG, logger);
        ugcSysTaskExecutor = ThreadPoolExecutorUtil.createExecutor(UGC_SYS_TASK_THREAD_POOL_CONFIG, logger);
    }

    private void initNotificationExecutor() {
        notificationExecutor = ThreadPoolExecutorUtil.createExecutor(NOTIFICATION_THREAD_POOL_CONFIG, logger);
    }

    private void initSystemExecutor() {
        recordOpLogExecutor = ThreadPoolExecutorUtil.createExecutor(RECORD_OP_LOG_THREAD_POOL_CONFIG, logger);
    }

    private void initUserExecutor() {
        userCommonOpExecutor = ThreadPoolExecutorUtil.createExecutor(USER_COMMON_OP_THREAD_POOL_CONFIG, logger);
    }

    private void destroyUgcExecutor() {
        ThreadPoolExecutorUtil.shutdownExecutor(auditUgcExecutor, "auditUgcExecutor");
        ThreadPoolExecutorUtil.shutdownExecutor(ugcStatisticsExecutor, "ugcStatisticsExecutor");
        ThreadPoolExecutorUtil.shutdownExecutor(ugcSysTaskExecutor, "ugcSysTaskExecutor");
    }

    private void destroyNotificationExecutor() {
        ThreadPoolExecutorUtil.shutdownExecutor(notificationExecutor, "notificationExecutor");
    }

    private void destroySystemExecutor() {
        ThreadPoolExecutorUtil.shutdownExecutor(recordOpLogExecutor, "recordOpLogExecutor");
    }

    private void destroyUserExecutor() {
        ThreadPoolExecutorUtil.shutdownExecutor(userCommonOpExecutor, "userCommonOpExecutor");
    }

}
