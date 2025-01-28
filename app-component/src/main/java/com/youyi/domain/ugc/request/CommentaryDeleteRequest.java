package com.youyi.domain.ugc.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
@Getter
@Setter
public class CommentaryDeleteRequest extends BaseRequest {

    @SerializedName("commentaryId")
    private String commentaryId;
}
