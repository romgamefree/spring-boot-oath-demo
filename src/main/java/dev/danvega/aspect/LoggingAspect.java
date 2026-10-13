package dev.danvega.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect ghi log thời gian thực thi cho tất cả các method
 * trong layer Controller và Service.
 *
 * <p>Thay thế hoàn toàn code thủ công đo thời gian như:</p>
 * <pre>{@code
 *   long start = System.nanoTime();
 *   // ... gọi method ...
 *   long end = System.nanoTime();
 *   System.out.printf("findById: %.2f ms%n", (end - start) / 1_000_000.0);
 * }</pre>
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ── Pointcut definitions ─────────────────────────────────────────────────

    /** Tất cả method trong package controller */
    @Pointcut("within(dev.danvega.controller..*)")
    public void inControllerLayer() {}

    /** Tất cả method trong package service */
    @Pointcut("within(dev.danvega.service..*)")
    public void inServiceLayer() {}

    /** Tất cả method trong controller hoặc service */
    @Pointcut("inControllerLayer() || inServiceLayer()")
    public void inApplicationLayer() {}

    // ── Advices ─────────────────────────────────────────────────────────────

    /**
     * Log tên method và thời gian thực thi (DEBUG) cho mọi method trong ứng dụng.
     */
    @Around("inApplicationLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className  = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        log.debug("[{}] Bắt đầu: {}()", className, methodName);

        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
            log.debug("[{}] Hoàn thành: {}() trong {} ms", className, methodName, elapsedMs);
            return result;
        } catch (Throwable ex) {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
            log.error("[{}] Lỗi: {}() sau {} ms – {}", className, methodName, elapsedMs, ex.getMessage());
            throw ex;
        }
    }
}
