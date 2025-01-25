package com.youyi.runner.ugc.model;

import com.google.gson.annotations.SerializedName;
import com.youyi.common.base.BaseResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcMetadataResponse extends BaseResponse {

    @SerializedName("ugcCategoryList")
    private List<UgcCategoryInfoResponse> ugcCategoryList;

    @SerializedName("ugcTagList")
    private List<UgcTagInfoResponse> ugcTagList;

    public static UgcMetadataResponse of(List<UgcCategoryInfoResponse> ugcCategoryList, List<UgcTagInfoResponse> ugcTagList) {
        UgcMetadataResponse response = new UgcMetadataResponse();
        response.ugcCategoryList = ugcCategoryList;
        response.ugcTagList = ugcTagList;
        return response;
    }
}
