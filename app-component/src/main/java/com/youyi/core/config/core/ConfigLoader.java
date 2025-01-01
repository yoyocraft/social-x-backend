package com.youyi.core.config.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.util.GsonUtil;
import com.youyi.core.config.domain.ConfigDO;
import com.youyi.core.config.repository.ConfigRepository;
import com.youyi.core.config.repository.po.ConfigPO;
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
import static com.youyi.core.config.assembler.ConfigAssembler.CONFIG_ASSEMBLER;

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
        .removalListener(notification -> LOGGER.warn("Cache entry removed: key:{},value:{}", notification.getKey(), GsonUtil.toJson(notification.getValue())))
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
            .map(CONFIG_ASSEMBLER::toDO)
            .collect(
                Collectors.toMap(
                    this::buildCacheKey,
                    ConfigDO::getConfigValue,
                    (o, n) -> n
                )
            );

        LOGGER.info("refresh config cache, size:{}, configs:{}", configMap.size(), GsonUtil.toJson(configMap));

        CONFIG_CENTER.putAll(configMap);
    }

    private String buildCacheKey(ConfigDO configDO) {
        return buildCacheKey(configDO.getConfigKey(), configDO.getEnv());
    }

    private String buildCacheKey(String configKey, String env) {
        return configKey + SymbolConstant.COLON + env;
    }
}
