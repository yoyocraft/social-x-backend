package com.youyi.common.base;

import com.youyi.common.type.ErrorCode;
import com.youyi.common.type.ReturnCode;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;

    /**
     * {@link ReturnCode}
     */
    private String code;

    private String message;

    private Long timestamp;

    private Result(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    private Result(T data, ErrorCode errorCode) {
        this.data = data;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    private Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    private Result(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> of(T data, String code, String message) {
        return new Result<>(data, code, message);
    }

    public static <T> Result<T> of(String code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> success() {
        return new Result<>(ReturnCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ReturnCode.SUCCESS);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    public static <T> Result<T> fail(String code, String message) {
        return new Result<>(code, message);
    }
}
