package com.youyi.domain.ugc.core;

import com.youyi.infra.conf.util.ThreadPoolExecutorUtil;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.youyi.infra.conf.core.ConfigKey.AUDIT_UGC_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.UGC_STATISTICS_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.ConfigKey.UGC_SYS_TASK_THREAD_POOL_CONFIG;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Component
public class UgcTpeContainer implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(UgcTpeContainer.class);

    private ThreadPoolExecutor auditUgcExecutor;

    private ThreadPoolExecutor ugcStatisticsExecutor;

    private ThreadPoolExecutor ugcSysTaskExecutor;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        initAsyncExecutor();
    }

    private void initAsyncExecutor() {
        initAuditUgcExecutor();
        initUgcStatisticsExecutor();
        initUgcDeleteTaskExecutor();
    }

    private void initAuditUgcExecutor() {
        auditUgcExecutor = ThreadPoolExecutorUtil.createExecutor(AUDIT_UGC_THREAD_POOL_CONFIG, logger);
    }

    private void initUgcStatisticsExecutor() {
        ugcStatisticsExecutor = ThreadPoolExecutorUtil.createExecutor(UGC_STATISTICS_THREAD_POOL_CONFIG, logger);
    }

    private void initUgcDeleteTaskExecutor() {
        ugcSysTaskExecutor = ThreadPoolExecutorUtil.createExecutor(UGC_SYS_TASK_THREAD_POOL_CONFIG, logger);
    }

    @Override
    public void destroy() {
        ThreadPoolExecutorUtil.shutdownExecutor(auditUgcExecutor, "auditUgcExecutor");
        ThreadPoolExecutorUtil.shutdownExecutor(ugcStatisticsExecutor, "ugcStatisticsExecutor");
        ThreadPoolExecutorUtil.shutdownExecutor(ugcSysTaskExecutor, "ugcSysTaskExecutor");
    }
}
