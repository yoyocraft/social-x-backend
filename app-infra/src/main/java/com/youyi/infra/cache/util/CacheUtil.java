package com.youyi.infra.cache.util;

import com.youyi.common.constant.SymbolConstant;
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

    public static String buildKey(String template, Map<String, String> dataMap) {
        StringSubstitutor substitutor = new StringSubstitutor(dataMap);
        return substitutor.replace(template);
    }
}
