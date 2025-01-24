package com.youyi.runner.ugc.model;

import com.youyi.common.base.BaseResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public class UgcResponse extends BaseResponse {

    private String ugcId;

    private String type;

    private String title;

    private String content;

    private String summary;

    private String cover;

    private List<String> attachmentUrls;

    private String authorId;

    private String authorName;

    private String authorAvatar;

    private Long viewCount;

    private Long likeCount;

    private Long commentCount;

    private String status;

    private List<String> tags;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
