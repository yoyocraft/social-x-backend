package com.youyi.infra.conf.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.util.GsonUtil;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.infra.conf.core.ConfigCache.getCacheRawValue;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/01
 */
public class SystemConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfigService.class);

    // ====================== map type ======================
    public static <K, V> Map<K, V> getMapConfig(String configKey, Class<K> keyType, Class<V> valueType) {
        return getMapConfig(configKey, keyType, valueType, Maps.newHashMap());
    }

    public static <K, V> Map<K, V> getMapConfig(String configKey, Class<K> keyType, Class<V> valueType, Map<K, V> defaultValue) {
        Type mapType = TypeToken.getParameterized(Map.class, keyType, valueType).getType();
        return getCacheValue(configKey, mapType, defaultValue);
    }

    // ====================== list type ======================
    public static <T> List<T> getListConfig(String configKey, Class<T> valueType) {
        return getListConfig(configKey, valueType, Lists.newArrayList());
    }

    public static <T> List<T> getListConfig(ConfigKey configKey, Class<T> valueType) {
        return getListConfig(configKey.name(), valueType, Lists.newArrayList());
    }

    public static <T> List<T> getListConfig(String configKey, Class<T> valueType, List<T> defaultValue) {
        Type listType = TypeToken.getParameterized(List.class, valueType).getType();
        return getCacheValue(configKey, listType, defaultValue);
    }

    // ====================== boolean type ======================

    public static Boolean getBooleanConfig(String key) {
        return getCacheValue(key, Boolean.class, java.lang.Boolean.FALSE);
    }

    public static Boolean getBooleanConfig(String key, Boolean defaultValue) {
        return getCacheValue(key, Boolean.class, defaultValue);
    }

    public static Boolean getBooleanConfig(ConfigKey configKey) {
        return getBooleanConfig(configKey, Boolean.FALSE);
    }

    public static Boolean getBooleanConfig(ConfigKey configKey, Boolean defaultValue) {
        return getBooleanConfig(configKey.name(), defaultValue);
    }

    // ====================== string type ======================

    public static String getStringConfig(String key) {
        return getStringConfig(key, StringUtils.EMPTY);
    }

    public static String getStringConfig(String key, String defaultValue) {
        return getCacheValue(key, String.class, defaultValue);
    }

    public static String getStringConfig(ConfigKey configKey) {
        return getStringConfig(configKey, StringUtils.EMPTY);
    }

    public static String getStringConfig(ConfigKey configKey, String defaultValue) {
        return getStringConfig(configKey.name(), defaultValue);
    }

    // ====================== long type ======================

    public static Long getLongConfig(String key) {
        return getLongConfig(key, 0L);
    }

    public static Long getLongConfig(String key, Long defaultValue) {
        return getCacheValue(key, Long.class, defaultValue);
    }

    public static Long getLongConfig(ConfigKey key) {
        return getLongConfig(key, 0L);
    }

    public static Long getLongConfig(ConfigKey key, Long defaultValue) {
        return getLongConfig(key.name(), defaultValue);
    }

    // ====================== integer type ======================

    public static Integer getIntegerConfig(String key) {
        return getIntegerConfig(key, 0);
    }

    public static Integer getIntegerConfig(String key, Integer defaultValue) {
        return getCacheValue(key, Integer.class, defaultValue);
    }

    public static Integer getIntegerConfig(ConfigKey key) {
        return getIntegerConfig(key, 0);
    }

    public static Integer getIntegerConfig(ConfigKey key, Integer defaultValue) {
        return getIntegerConfig(key.name(), defaultValue);
    }

    // ====================== class type ======================

    public static <T> T getCacheValue(String configKey, Class<T> type) {
        return getCacheValue(configKey, type, null);
    }

    public static <T> T getCacheValue(ConfigKey configKey, Class<T> type) {
        return getCacheValue(configKey, type, null);
    }

    public static <T> T getCacheValue(ConfigKey configKey, Class<T> type, T defaultValue) {
        return getCacheValue(configKey.name(), type, defaultValue);
    }

    public static <T> T getCacheValue(String configKey, Class<T> type, T defaultValue) {
        String value = getCacheRawValue(configKey);

        if (value == null) {
            LOGGER.warn("Config not found for key:{}. Returning default value: {}", configKey, defaultValue);
            return defaultValue;
        }

        try {
            if (type.isPrimitive() || type == String.class) {
                return type.cast(value);
            }
            return GsonUtil.fromJson(value, type);
        } catch (Exception e) {
            LOGGER.error("Error parsing cache value for key:{}, expected type:{}", configKey, type.getName(), e);
            return defaultValue;
        }
    }

    // ====================== reflect type ======================

    public static <T> T getCacheValue(String configKey, Type type) {
        return getCacheValue(configKey, type, null);
    }

    public static <T> T getCacheValue(String configKey, Type type, T defaultValue) {
        String value = getCacheRawValue(configKey);

        if (value == null) {
            LOGGER.warn("Config not found for key:{}. Returning default value: {}", configKey, defaultValue);
            return defaultValue;
        }

        try {
            return GsonUtil.fromJson(value, type);
        } catch (Exception e) {
            LOGGER.error("Error parsing cache value for key:{}, expected type: {}", configKey, type.getTypeName(), e);
            return defaultValue;
        }
    }
}


