package com.youyi.infra.conf.util;

import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.infra.conf.core.Conf.getCacheValue;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/19
 */
public class ThreadPoolExecutorUtil {
    private static final Logger innerLogger = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    /**
     * 创建线程池
     *
     * @param configKey 配置项
     * @return ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor createExecutor(ConfigKey configKey, Logger logger) {
        ThreadPoolConfigWrapper config = getCacheValue(configKey, ThreadPoolConfigWrapper.class);

        if (config == null) {
            innerLogger.error("Failed to initialize {} thread pool: Config not found", configKey.name());
            throw new IllegalStateException("Missing thread pool config: " + configKey.name());
        }

        innerLogger.info("Initializing {} thread pool with config: {}", configKey.name(), config);

        return new ThreadPoolExecutor(
            config.getCorePoolSize(),
            config.getMaximumPoolSize(),
            config.getKeepAliveTime(),
            config.getTimeUnit(),
            config.getQueue(),
            config.getThreadFactory(logger),
            config.getRejectedHandler()
        );
    }

    /**
     * 关闭线程池
     *
     * @param executor 线程池实例
     * @param name     线程池名称
     */
    public static void shutdownExecutor(ThreadPoolExecutor executor, String name) {
        if (executor != null) {
            executor.shutdown();
            innerLogger.info("{} has been shut down successfully.", name);
        }
    }
}
