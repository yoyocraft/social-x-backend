package com.youyi.core.config.core;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.Env;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/01
 */
class ConfigCacheServiceTest {
    @Mock
    ConfigLoader configLoader;
    @InjectMocks
    ConfigCacheService configCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // init data
        Map<String, String> dataMap = ImmutableMap.of(
            buildCacheKey("STRING_KEY"), "STRING_VALUE",
            buildCacheKey("BOOLEAN_KEY"), "true",
            buildCacheKey("LONG_KEY"), "123",
            buildCacheKey("JSON_KEY"), "{\"key\":\"value\"}",
            buildCacheKey("LIST_KEY"), "[1,2,3]",
            buildCacheKey("MAP_KEY"), "{\"key\":\"value\"}"
        );

        when(configLoader.getCacheRawValue(anyString())).thenAnswer(invocation -> {
            String cacheKey = invocation.getArgument(0, String.class);
            return dataMap.get(cacheKey);
        });
    }

    @Test
    void testGetCacheValue() {
        Optional<Boolean> booleanOptional = configCacheService.getCacheBoolean("BOOLEAN_KEY");
        Assertions.assertTrue(booleanOptional.isPresent() && booleanOptional.get());

        Optional<List<Integer>> listOptional = configCacheService.getCacheList("LIST_KEY", Integer.class);
        Assertions.assertTrue(
            listOptional.isPresent()
                && listOptional.get().size() == 3
                && listOptional.get().get(0) == 1
                && listOptional.get().get(1) == 2
                && listOptional.get().get(2) == 3
        );

        Optional<Map<String, String>> mapOptional = configCacheService.getCacheMap("MAP_KEY", String.class, String.class);
        Assertions.assertTrue(
            mapOptional.isPresent()
                && mapOptional.get().size() == 1
                && "value".equals(mapOptional.get().get("key"))
        );

        Optional<String> stringOptional = configCacheService.getCacheString("STRING_KEY");
        Assertions.assertTrue(stringOptional.isPresent() && "STRING_VALUE".equals(stringOptional.get()));

        Optional<Long> longOptional = configCacheService.getCacheLong("LONG_KEY");
        Assertions.assertTrue(longOptional.isPresent() && longOptional.get() == 123);

        Optional<TestEntry> entryOptional = configCacheService.getCacheValue("JSON_KEY", TestEntry.class);
        Assertions.assertTrue(entryOptional.isPresent() && "value".equals(entryOptional.get().getKey()));
    }

    @Getter
    @Setter
    static class TestEntry {
        private String key;
    }

    private String buildCacheKey(String key) {
        return key + SymbolConstant.COLON + Env.LOCAL.name();
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme