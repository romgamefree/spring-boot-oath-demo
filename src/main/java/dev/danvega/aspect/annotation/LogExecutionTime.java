package dev.danvega.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation để đo thời gian thực thi của method.
 * Sử dụng cùng với {@link dev.danvega.aspect.PerformanceAspect}.
 *
 * <p>Ví dụ sử dụng:</p>
 * <pre>{@code
 * @LogExecutionTime
 * public void myMethod() { ... }
 *
 * @LogExecutionTime(warnThresholdMs = 200)
 * public void slowMethod() { ... }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    /**
     * Ngưỡng cảnh báo (ms). Method vượt ngưỡng này sẽ log ở mức WARN.
     * Mặc định: 500ms.
     */
    long warnThresholdMs() default 500L;

    /**
     * Mô tả tùy chỉnh cho operation (tùy chọn).
     */
    String value() default "";
}
