package com.youyi.common.annotation;

import com.youyi.common.type.OperationType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordOpLog {

    /**
     * 操作类型
     */
    OperationType opType();

    /**
     * 是否是系统操作，记录 operatorId 和 operatorName
     */
    boolean system() default false;

    /**
     * 是否脱敏
     */
    boolean desensitize() default false;
}
