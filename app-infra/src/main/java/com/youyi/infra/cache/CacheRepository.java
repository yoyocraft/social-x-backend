package com.youyi.infra.cache;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
@Component
@RequiredArgsConstructor
public class CacheRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(CacheRepository.class);

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.REDIS;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.REDIS_ERROR;
    }

    // ============================= STRING OPS =================================
    public void set(String key, Object value) {
        executeWithExceptionHandling(() -> redisTemplate.opsForValue().set(key, value));
    }

    public void set(String key, Object value, long timeout) {
        executeWithExceptionHandling(() -> redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS));
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        executeWithExceptionHandling(() -> redisTemplate.opsForValue().set(key, value, timeout, timeUnit));
    }

    public void set(String key, Object value, Duration timeout) {
        executeWithExceptionHandling(() -> redisTemplate.opsForValue().set(key, value, timeout));
    }

    public Object get(String key) {
        return executeWithExceptionHandling(() -> redisTemplate.opsForValue().get(key));
    }

    public String getString(String key) {
        return executeWithExceptionHandling(() -> (String) redisTemplate.opsForValue().get(key));
    }

    public Object get(String key, Object defaultValue) {
        Object value = get(key);
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    // ============================= SET OPS =================================

    public void addToSet(String key, Object value, Duration timeout) {
        executeWithExceptionHandling(() -> {
            redisTemplate.opsForSet().add(key, value);
            redisTemplate.expire(key, timeout);
        });
    }

    public void addToSet(String key, Duration timeout, Object... values) {
        executeWithExceptionHandling(() -> {
            redisTemplate.opsForSet().add(key, values);
            redisTemplate.expire(key, timeout);
        });
    }

    public void removeFromSet(String key, Object value) {
        executeWithExceptionHandling(() -> redisTemplate.opsForSet().remove(key, value));
    }

    public Set<Object> getSetMembers(String key) {
        return executeWithExceptionHandling(() -> redisTemplate.opsForSet().members(key));
    }

    // ============================= DELETE OPS =================================

    public void delete(String key) {
        executeWithExceptionHandling(() -> redisTemplate.delete(key));
    }

    public void batchDelete(List<String> keys) {
        executeWithExceptionHandling(() -> redisTemplate.delete(keys));
    }

    // ============================= EXISTS OPS =================================
    public boolean exists(String key) {
        return executeWithExceptionHandling(() -> Boolean.TRUE.equals(redisTemplate.hasKey(key)));
    }

    // ============================= LUA SCRIPT OPS =================================
    public <T> T execute(Class<T> returnType, String luaScript, String key, Object... args) {
        return executeWithExceptionHandling(() -> {
            DefaultRedisScript<T> script = new DefaultRedisScript<>();
            script.setScriptText(luaScript);
            script.setResultType(returnType);
            return redisTemplate.execute(script, Collections.singletonList(key), args);
        });
    }

// ============================= Pipeline OPS =================================

    public List<Object> getPipelineResult(List<String> keys) {
        return executeWithExceptionHandling(() ->
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : keys) {
                    connection.get(key.getBytes());
                }
                return null;
            }));
    }

}
