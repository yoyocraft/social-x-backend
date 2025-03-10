package com.youyi.infra.detect.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import static com.youyi.common.constant.ErrorCodeConstant.SUCCESS;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
@Getter
@Setter
public class ImageDetectResponse implements Serializable {

    public static final ImageDetectResponse DEFAULT_INSTANCE = new ImageDetectResponse();

    static {
        DEFAULT_INSTANCE.setCode(1);
        DEFAULT_INSTANCE.setMessage(SUCCESS);
        DEFAULT_INSTANCE.setData(List.of());
    }

    @Serial private static final long serialVersionUID = 1L;

    @SerializedName("code")
    private Integer code;

    @SerializedName("msg")
    private String message;

    @SerializedName("data")
    private List<DetectResult> data;

    @Getter
    @Setter
    public static class DetectResult {

        @SerializedName("img")
        private String imageKey;

        @SerializedName("img_classify_result")
        private List<ImageClassifyInfo> imageClassifyResult;

        @SerializedName("ocr_result")
        private String ocrResult;

        @SerializedName("sensitive_detect_result")
        private SensitiveDetectResult sensitiveDetectResult;
    }

    @Getter
    @Setter
    public static class ImageClassifyInfo {
        @SerializedName("label")
        private String label;

        @SerializedName("prob")
        private Double prob;
    }

    @Getter
    @Setter
    public static class SensitiveDetectResult {
        @SerializedName("category")
        private String category;

        @SerializedName("keyword")
        private Boolean keyword;
    }
}
