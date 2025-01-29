package com.youyi.infra.cache.manager;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
@Component
@RequiredArgsConstructor
public class CacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public void set(String key, Object value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public void set(String key, Object value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public String getString(String key) {
        try {
            return (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public Object get(String key, Object defaultValue) {
        Object value = get(key);
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }

    public <T> T execute(Class<T> returnType, String luaScript, String key, Object... args) {
        try {
            DefaultRedisScript<T> script = new DefaultRedisScript<>();
            script.setScriptText(luaScript);
            script.setResultType(returnType);
            return redisTemplate.execute(script, Collections.singletonList(key), args);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.REDIS, InfraCode.REDIS_ERROR, e);
            throw AppSystemException.of(InfraCode.REDIS_ERROR, e);
        }
    }
}
