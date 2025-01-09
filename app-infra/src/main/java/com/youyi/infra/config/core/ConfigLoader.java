package com.youyi.infra.config.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.infra.config.repository.ConfigRepository;
import com.youyi.infra.config.repository.po.ConfigPO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.LogUtil.runWithCost;

/**
 * Config loader using local memory cache
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/01
 */
@Component
@RequiredArgsConstructor
public class ConfigLoader implements SmartInitializingSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final int CONFIG_CACHE_SIZE = 10000;
    private static final int CONFIG_CACHE_REFRESH_INTERVAL = 10000;

    private static final Cache<String, String> CONFIG_CENTER = CacheBuilder
        .newBuilder()
        .maximumSize(CONFIG_CACHE_SIZE)
        .build();

    private final ConfigRepository configRepository;

    @Override
    public void afterSingletonsInstantiated() {
        refreshCache();
    }

    @Scheduled(fixedDelay = CONFIG_CACHE_REFRESH_INTERVAL)
    public void refreshCache() {
        try {
            runWithCost(LOGGER, this::doRefreshConfigCache, "refreshConfigCache");
        } catch (Exception e) {
            LOGGER.error("[ConfigLoader] refreshCache exp", e);
        }
    }

    public String getCacheRawValue(String key) {
        return CONFIG_CENTER.getIfPresent(key);
    }

    @VisibleForTesting
    protected void doRefreshConfigCache() {
        List<ConfigPO> configPOList = configRepository.queryAllConfig();
        if (CollectionUtils.isEmpty(configPOList)) {
            return;
        }

        Map<String, String> configMap = configPOList.stream()
            .collect(
                Collectors.toMap(
                    this::buildCacheKey,
                    ConfigPO::getConfigValue,
                    (o, n) -> n
                )
            );

        CONFIG_CENTER.putAll(configMap);
    }

    private String buildCacheKey(ConfigPO po) {
        return buildCacheKey(po.getConfigKey(), po.getEnv());
    }

    private String buildCacheKey(String configKey, String env) {
        return configKey + SymbolConstant.COLON + env;
    }
}
