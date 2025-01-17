package com.youyi.common.type.media;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
public enum ResourceType {

    UNKNOWN,
    IMAGE,
    FILE,
    ;

    public static ResourceType of(String type) {
        return EnumUtils.getEnum(ResourceType.class, type, UNKNOWN);
    }

    public String getType() {
        return name().toLowerCase();
    }
}
