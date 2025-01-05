package com.youyi.infra.config.core;

import com.google.gson.reflect.TypeToken;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.Env;
import com.youyi.common.util.GsonUtil;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/01
 */
@Component
@RequiredArgsConstructor
public class ConfigCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigCacheService.class);

    private final ConfigLoader configLoader;

    public <T> Optional<T> getCacheValue(String configKey, Class<T> type) {
        return getCacheValue(configKey, Env.LOCAL.name(), type);
    }

    public <T> Optional<T> getCacheValue(String configKey, String env, Class<T> type) {
        String key = buildCacheKey(configKey, env);
        String value = configLoader.getCacheRawValue(key);

        if (value == null) {
            LOGGER.warn("Cache miss for key: {}", key);
            return Optional.empty();
        }

        try {
            T result = GsonUtil.fromJson(value, type);
            return Optional.of(result);
        } catch (Exception e) {
            LOGGER.error("Error parsing cache value for key: {}, expected type: {}", key, type.getName(), e);
        }

        LOGGER.warn("Cache value for key: {} is not of expected type: {}", key, type.getName());
        return Optional.empty();
    }

    public <T> Optional<T> getCacheValue(String configKey, Type type) {
        return getCacheValue(configKey, Env.LOCAL.name(), type);
    }

    public <T> Optional<T> getCacheValue(String configKey, String env, Type type) {
        String key = buildCacheKey(configKey, env);
        String value = configLoader.getCacheRawValue(key);

        if (value == null) {
            LOGGER.warn("Cache miss for key: {}", key);
            return Optional.empty();
        }

        try {
            T result = GsonUtil.fromJson(value, type);
            return Optional.of(result);
        } catch (Exception e) {
            LOGGER.error("Error parsing cache value for key: {}, expected type: {}", key, type.getTypeName(), e);
        }

        LOGGER.warn("Cache value for key: {} is not of expected type: {}", key, type.getTypeName());
        return Optional.empty();
    }

    public <K, V> Optional<Map<K, V>> getCacheMap(String configKey, Class<K> keyType, Class<V> valueType) {
        Type mapType = TypeToken.getParameterized(Map.class, keyType, valueType).getType();
        return getCacheValue(configKey, mapType);
    }

    public <T> Optional<List<T>> getCacheList(String configKey, Class<T> valueType) {
        Type listType = TypeToken.getParameterized(List.class, valueType).getType();
        return getCacheValue(configKey, listType);
    }

    public Optional<Boolean> getCacheBoolean(String key) {
        return getCacheValue(key, Boolean.class);
    }

    public Optional<String> getCacheString(String key) {
        return getCacheValue(key, String.class);
    }

    public Optional<Long> getCacheLong(String key) {
        return getCacheValue(key, Long.class);
    }

    private String buildCacheKey(String configKey, String env) {
        return configKey + SymbolConstant.COLON + env;
    }
}

