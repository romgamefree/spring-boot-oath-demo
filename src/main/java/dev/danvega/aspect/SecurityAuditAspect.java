package dev.danvega.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect kiểm toán bảo mật (Security Audit) cho các hành động trong Admin layer.
 *
 * <p>Tự động ghi lại:</p>
 * <ul>
 *   <li>Ai thực hiện thao tác (username từ SecurityContext)</li>
 *   <li>Method nào được gọi và với tham số gì</li>
 *   <li>Kết quả thành công hay thất bại</li>
 * </ul>
 *
 * <p>Các hành động được theo dõi:</p>
 * <ul>
 *   <li>Tạo / cập nhật / xóa Article</li>
 *   <li>Các thao tác write trong AdminController</li>
 * </ul>
 */
@Aspect
@Component
public class SecurityAuditAspect {

    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT." + SecurityAuditAspect.class.getName());

    // ── Pointcuts ────────────────────────────────────────────────────────────

    /** Mọi method save/update trong ArticleService */
    @Pointcut("execution(* dev.danvega.service.ArticleService.save(..))")
    public void articleSave() {}

    /** Method xóa trong ArticleService */
    @Pointcut("execution(* dev.danvega.service.ArticleService.deleteById(..))")
    public void articleDelete() {}

    /** POST handler trong AdminArticleController (tạo + cập nhật + xóa) */
    @Pointcut("within(dev.danvega.controller.AdminArticleController) "
            + "&& @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void adminArticlePostActions() {}

    /** Hợp nhất tất cả điểm audit */
    @Pointcut("articleSave() || articleDelete() || adminArticlePostActions()")
    public void auditableActions() {}

    // ── Advices ──────────────────────────────────────────────────────────────

    /**
     * Ghi log TRƯỚC khi thực thi hành động (Before Advice).
     */
    @Before("auditableActions()")
    public void logBefore(JoinPoint joinPoint) {
        String actor  = getCurrentUsername();
        String method = buildMethodLabel(joinPoint);
        String args   = buildArgsPreview(joinPoint.getArgs());

        auditLog.info("[AUDIT] {} ▶ {} | args: {}", actor, method, args);
    }

    /**
     * Ghi log SAU KHI thực thi thành công (AfterReturning Advice).
     */
    @AfterReturning(pointcut = "auditableActions()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        String actor  = getCurrentUsername();
        String method = buildMethodLabel(joinPoint);
        String returnVal = result != null ? result.toString() : "void";

        auditLog.info("[AUDIT] {} ✓ {} | kết quả: {}", actor, method, truncate(returnVal, 120));
    }

    /**
     * Ghi log khi method ném ngoại lệ (AfterThrowing Advice).
     */
    @AfterThrowing(pointcut = "auditableActions()", throwing = "ex")
    public void logAfterFailure(JoinPoint joinPoint, Throwable ex) {
        String actor  = getCurrentUsername();
        String method = buildMethodLabel(joinPoint);

        auditLog.error("[AUDIT] {} ✗ {} | lỗi: {} – {}",
                actor, method, ex.getClass().getSimpleName(), ex.getMessage());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Lấy username từ SecurityContext; trả về "anonymous" nếu chưa xác thực. */
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "anonymous";
        }
        return auth.getName();
    }

    /** Tạo nhãn "ClassName.methodName" cho JoinPoint. */
    private String buildMethodLabel(JoinPoint joinPoint) {
        String cls = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String mtd = joinPoint.getSignature().getName();
        return cls + "." + mtd + "()";
    }

    /** Tóm tắt danh sách tham số thành chuỗi ngắn gọn. */
    private String buildArgsPreview(Object[] args) {
        if (args == null || args.length == 0) return "(none)";
        return truncate(Arrays.toString(args), 200);
    }

    /** Cắt ngắn chuỗi nếu quá dài. */
    private String truncate(String s, int maxLength) {
        if (s == null) return "null";
        return s.length() <= maxLength ? s : s.substring(0, maxLength) + "…";
    }
}
