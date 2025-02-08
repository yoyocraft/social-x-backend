package com.youyi.domain.notification.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/08
 */
@Getter
@Setter
public class NotificationExtraData {

    /**
     * ugc id, comment id, etc.
     */
    private String targetId;

    private String summary;

    private String content;
}
