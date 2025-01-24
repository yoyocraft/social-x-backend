package com.youyi.domain.ugc.request;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseRequest;
import com.youyi.common.type.ugc.UgcStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Getter
@Setter
public class UgcSetStatusRequest extends BaseRequest {

    @SerializedName("ugcId")
    private String ugcId;

    /**
     * @see UgcStatusType#PRIVATE,UgcStatusType#PUBLISHED
     */
    @SerializedName("status")
    private String status;

}
