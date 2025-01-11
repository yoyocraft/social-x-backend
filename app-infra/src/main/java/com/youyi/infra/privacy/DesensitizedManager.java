package com.youyi.infra.privacy;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/11
 */
public class DesensitizedManager {

    private DesensitizedManager() {
    }

    public static String desensitizeEmail(String originalEmail) {
        return DesensitizedUtil.email(originalEmail);
    }

    public static String desensitizeMobile(String originalMobilePhone) {
        return DesensitizedUtil.mobilePhone(originalMobilePhone);
    }
}
