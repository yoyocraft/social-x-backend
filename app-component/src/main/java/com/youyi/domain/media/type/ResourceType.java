package com.youyi.domain.media.type;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
public enum ResourceType {

    UNKNOWN("unknown"),
    IMAGE("image"),
    FILE("file"),
    ;

    private final String type;

    ResourceType(String type) {
        this.type = type;
    }

    public static ResourceType of(String type) {
        return EnumUtils.getEnum(ResourceType.class, type, UNKNOWN);
    }
}
