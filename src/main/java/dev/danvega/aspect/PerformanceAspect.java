//package dev.danvega.aspect;
//
//import dev.danvega.aspect.annotation.LogExecutionTime;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * Aspect đo hiệu năng và cảnh báo khi method thực thi chậm.
// *
// * <p>Hoạt động theo hai cách:</p>
// * <ol>
// *   <li>Tự động áp dụng cho tất cả method Service với ngưỡng mặc định 1000ms</li>
// *   <li>Áp dụng theo annotation {@link LogExecutionTime} với ngưỡng tùy chỉnh</li>
// * </ol>
// */
//@Aspect
//@Component
//public class PerformanceAspect {
//
//    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);
//
//    /** Ngưỡng mặc định để cảnh báo hiệu năng (ms) cho Service layer */
//    private static final long DEFAULT_WARN_THRESHOLD_MS = 1000L;
//
//    // ── Pointcuts ────────────────────────────────────────────────────────────
//
//    /** Method có annotation @LogExecutionTime */
//    @Pointcut("@annotation(dev.danvega.aspect.annotation.LogExecutionTime)")
//    public void annotatedWithLogExecutionTime() {}
//
//    /** Tất cả public method trong Service */
//    @Pointcut("execution(public * dev.danvega.service..*(..))")
//    public void publicServiceMethods() {}
//
//    // ── Advices ──────────────────────────────────────────────────────────────
//
//    /**
//     * Đo thời gian và cảnh báo khi method được đánh dấu {@code @LogExecutionTime}
//     * vượt ngưỡng được cấu hình.
//     */
//    @Around("annotatedWithLogExecutionTime()")
//    public Object measureAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//
//        LogExecutionTime annotation = method.getAnnotation(LogExecutionTime.class);
//        long warnThreshold = annotation.warnThresholdMs();
//        String label = annotation.value().isBlank()
//                ? signature.getDeclaringType().getSimpleName() + "." + method.getName()
//                : annotation.value();
//
//        return executeAndLog(joinPoint, label, warnThreshold);
//    }
//
//    /**
//     * Tự động đo và cảnh báo cho tất cả public method trong Service layer
//     * nếu chưa được xử lý bởi advice annotation.
//     */
//    @Around("publicServiceMethods() && !annotatedWithLogExecutionTime()")
//    public Object measureServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String label = signature.getDeclaringType().getSimpleName()
//                + "." + signature.getName();
//
//        return executeAndLog(joinPoint, label, DEFAULT_WARN_THRESHOLD_MS);
//    }
//
//    // ── Helper ───────────────────────────────────────────────────────────────
//
//    private Object executeAndLog(ProceedingJoinPoint joinPoint,
//                                 String label,
//                                 long warnThresholdMs) throws Throwable {
//        long start = System.nanoTime();
//        try {
//            Object result = joinPoint.proceed();
//            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
//
//            if (elapsedMs >= warnThresholdMs) {
//                log.warn("⚠ CHẬM [{}]: {} ms (ngưỡng: {} ms)", label, elapsedMs, warnThresholdMs);
//            } else {
//                log.debug("✓ [{}]: {} ms", label, elapsedMs);
//            }
//
//            return result;
//        } catch (Throwable ex) {
//            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
//            log.error("✗ [{}] thất bại sau {} ms: {}", label, elapsedMs, ex.getMessage());
//            throw ex;
//        }
//    }
//}
