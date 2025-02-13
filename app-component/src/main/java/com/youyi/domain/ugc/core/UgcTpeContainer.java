package com.youyi.domain.ugc.core;

import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.conf.ConfigKey.AUDIT_UGC_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.Conf.checkConfig;
import static com.youyi.infra.conf.core.Conf.getCacheValue;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Component
public class UgcTpeContainer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcTpeContainer.class);

    private ThreadPoolExecutor auditUgcExecutor;

    private ThreadPoolExecutor ugcStatisticsExecutor;

    private ThreadPoolExecutor ugcSysTaskExecutor;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkTpeConfig();
        initAsyncExecutor();
    }

    private void initAsyncExecutor() {
        initAuditUgcExecutor();
        initUgcStatisticsExecutor();
        initUgcDeleteTaskExecutor();
    }

    private void initAuditUgcExecutor() {
        ThreadPoolConfigWrapper auditUgcTpeConfig = getCacheValue(AUDIT_UGC_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        auditUgcExecutor = new ThreadPoolExecutor(
            auditUgcTpeConfig.getCorePoolSize(),
            auditUgcTpeConfig.getMaximumPoolSize(),
            auditUgcTpeConfig.getKeepAliveTime(),
            auditUgcTpeConfig.getTimeUnit(),
            auditUgcTpeConfig.getQueue(),
            auditUgcTpeConfig.getThreadFactory(LOGGER),
            auditUgcTpeConfig.getRejectedHandler()
        );
    }

    private void initUgcStatisticsExecutor() {
        ThreadPoolConfigWrapper ugcStatisticsTpeConfig = getCacheValue(ConfigKey.UGC_STATISTICS_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        ugcStatisticsExecutor = new ThreadPoolExecutor(
            ugcStatisticsTpeConfig.getCorePoolSize(),
            ugcStatisticsTpeConfig.getMaximumPoolSize(),
            ugcStatisticsTpeConfig.getKeepAliveTime(),
            ugcStatisticsTpeConfig.getTimeUnit(),
            ugcStatisticsTpeConfig.getQueue(),
            ugcStatisticsTpeConfig.getThreadFactory(LOGGER),
            ugcStatisticsTpeConfig.getRejectedHandler()
        );
    }

    private void initUgcDeleteTaskExecutor() {
        ThreadPoolConfigWrapper ugcDeleteTaskTpeConfig = getCacheValue(ConfigKey.UGC_SYS_TASK_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        ugcSysTaskExecutor = new ThreadPoolExecutor(
            ugcDeleteTaskTpeConfig.getCorePoolSize(),
            ugcDeleteTaskTpeConfig.getMaximumPoolSize(),
            ugcDeleteTaskTpeConfig.getKeepAliveTime(),
            ugcDeleteTaskTpeConfig.getTimeUnit(),
            ugcDeleteTaskTpeConfig.getQueue(),
            ugcDeleteTaskTpeConfig.getThreadFactory(LOGGER),
            ugcDeleteTaskTpeConfig.getRejectedHandler()
        );
    }

    private void checkTpeConfig() {
        checkConfig(ConfigKey.AUDIT_UGC_THREAD_POOL_CONFIG);
        checkConfig(ConfigKey.UGC_STATISTICS_THREAD_POOL_CONFIG);
        checkConfig(ConfigKey.UGC_SYS_TASK_THREAD_POOL_CONFIG);
    }
}
