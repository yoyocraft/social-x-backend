package com.youyi.domain.ugc.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Getter
@Setter
public class UgcMetadataDO {

    private List<UgcTagInfo> ugcTagList;

    private List<UgcCategoryInfo> ugcCategoryList;

    // for query
    private long cursor;
    private int size;

    public static UgcMetadataDO of(List<UgcCategoryInfo> ugcCategoryList, List<UgcTagInfo> ugcTagList) {
        UgcMetadataDO ugcMetadataDO = new UgcMetadataDO();
        ugcMetadataDO.ugcCategoryList = ugcCategoryList;
        ugcMetadataDO.ugcTagList = ugcTagList;
        return ugcMetadataDO;
    }
}
