package com.youyi.runner.ugc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryInfo {

    private String commentaryId;
    private String parentId;
    private String ugcId;
    private String commentatorId;
    private String commentatorNickName;
    private String commentatorAvatar;
    private String commentary;
    private Long likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private String status;
}
