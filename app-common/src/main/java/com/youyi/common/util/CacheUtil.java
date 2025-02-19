package com.youyi.common.util;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.cache.CacheKey;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/10
 */
public class CacheUtil {

    public static String ofKey(String... keys) {
        return StringUtils.join(keys, SymbolConstant.COLON);
    }

    public static String buildKey(CacheKey cacheKey, Map<String, String> dataMap) {
        if (cacheKey.isPattern()) {
            StringSubstitutor substitutor = new StringSubstitutor(dataMap);
            return substitutor.replace(cacheKey.getKey());
        }
        return cacheKey.getKey();
    }
}
