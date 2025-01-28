package com.youyi.domain.ugc.util;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.constant.SymbolConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
public class AuditUtil {

    private static final String SENSITIVE_CHECK_APPENDER = "包含敏感词；";

    /**
     * 检查字段内容是否包含敏感词
     */
    public static boolean checkSensitiveContent(String fieldName, String content, StringBuilder auditRetBuilder) {
        if (StringUtils.isNotBlank(content) && SensitiveWordHelper.contains(content)) {
            auditRetBuilder
                .append(fieldName)
                .append(SENSITIVE_CHECK_APPENDER)
                .append(SymbolConstant.NEW_LINE);
            return true;
        }
        return false;
    }
}
