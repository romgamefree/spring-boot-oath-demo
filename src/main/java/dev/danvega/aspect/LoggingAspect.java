//package dev.danvega.aspect;
//
//import java.util.Arrays;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Aspect
//@Slf4j
//@Component
//public class LoggingAspect {
//
//    /**
//     * Pointcut that matches repositories, services and REST controllers.
//     */
//    @Pointcut(
//            "within(@org.springframework.stereotype.Repository *)"
//                    + " || within(@org.springframework.stereotype.Service *)"
//                    + " || within(@org.springframework.web.bind.annotation.RestController *)"
//    )
//    public void springBeanPointcut() {
//    }
//
//    /**
//     * Pointcut that matches application service, repository and controller packages.
//     */
//    @Pointcut(
//            "within(dev.danvega.service..*)"
//                    + " || within(dev.danvega.repository..*)"
//                    + " || within(dev.danvega.controller..*)"
//    )
//    public void applicationPackagePointcut() {
//    }
//
//    /**
//     * Advice that logs methods throwing exceptions.
//     */
//    @AfterThrowing(
//            pointcut = "applicationPackagePointcut() && springBeanPointcut()",
//            throwing = "e"
//    )
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        log.error(
//                "Exception in {}.{}() with cause = {}",
//                joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(),
//                e.getCause() != null ? e.getCause() : "NULL"
//        );
//    }
//
//    /**
//     * Advice that logs when a method is entered and exited.
//     */
//    @Around("applicationPackagePointcut() && springBeanPointcut()")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (log.isDebugEnabled()) {
//            log.debug(
//                    "Enter: {}.{}() with argument[s] = {}",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(),
//                    Arrays.toString(joinPoint.getArgs())
//            );
//        }
//
//        try {
//            Object result = joinPoint.proceed();
//
//            if (log.isDebugEnabled()) {
//                log.debug(
//                        "Exit: {}.{}()",
//                        joinPoint.getSignature().getDeclaringTypeName(),
//                        joinPoint.getSignature().getName()
//                );
//            }
//
//            return result;
//        } catch (IllegalArgumentException e) {
//            log.error(
//                    "Illegal argument: {} in {}.{}()",
//                    Arrays.toString(joinPoint.getArgs()),
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName()
//            );
//
//            throw e;
//        }
//    }
//
//    @Pointcut("within(dev.danvega.controller..*)")
//    public void controllerPointcut() {
//    }
//
//    /**
//     * Advice that logs HTTP requests and controller execution time.
//     */
//    @Around("controllerPointcut()")
//    public Object logAroundController(ProceedingJoinPoint joinPoint)
//            throws Throwable {
//
//        RequestAttributes requestAttributes =
//                RequestContextHolder.getRequestAttributes();
//
//        if (requestAttributes != null) {
//            ServletRequestAttributes attributes =
//                    (ServletRequestAttributes) requestAttributes;
//
//            HttpServletRequest request = attributes.getRequest();
//
//            log.info(
//                    "---> {} {} {}",
//                    request.getMethod(),
//                    request.getRequestURL(),
//                    request.getProtocol()
//            );
//        }
//
//        long start = System.currentTimeMillis();
//
//        try {
//            String className =
//                    joinPoint.getSignature().getDeclaringTypeName();
//            String methodName =
//                    joinPoint.getSignature().getName();
//
//            Object result = joinPoint.proceed();
//
//            long elapsedTime = System.currentTimeMillis() - start;
//
//            log.debug(
//                    "Execution Time: {}.{}() = {} ms",
//                    className,
//                    methodName,
//                    elapsedTime
//            );
//
//            return result;
//        } catch (IllegalArgumentException e) {
//            log.error(
//                    "Illegal argument {} in {}()",
//                    Arrays.toString(joinPoint.getArgs()),
//                    joinPoint.getSignature().getName()
//            );
//
//            throw e;
//        }
//    }
//}