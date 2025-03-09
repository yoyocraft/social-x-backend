package com.youyi.common.type.media;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
public enum MediaSource {

    UNKNOWN,
    AVATAR,
    ARTICLE,
    POST,
    QUESTION,
    REPLY,
    SYSTEM,
    ;

    public static MediaSource of(String type) {
        return EnumUtils.getEnum(MediaSource.class, type, UNKNOWN);
    }

    public String getSource() {
        return this.name().toLowerCase();
    }
}
