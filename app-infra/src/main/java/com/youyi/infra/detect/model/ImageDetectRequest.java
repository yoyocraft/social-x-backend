package com.youyi.infra.detect.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
@Getter
@Setter
public class ImageDetectRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @SerializedName("images")
    private List<ImageInfo> images;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ImageInfo {
        @SerializedName("key")
        private String key;

        @SerializedName("base64")
        private String base64;
    }

}
