package com.youyi.runner.ugc.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryResponse {

    private CommentaryInfo topCommentary;
    private List<CommentaryInfo> replyList;
}
