package dev.danvega.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Global Exception Handler – xử lý tập trung mọi exception trong ứng dụng.
 *
 * <h3>Chiến lược phân loại response:</h3>
 * <ul>
 *   <li><b>REST (Accept: application/json)</b> → trả về {@link ErrorResponse} JSON</li>
 *   <li><b>HTMX (HX-Request: true)</b> → trả về HTML fragment toast qua HX-Retarget</li>
 *   <li><b>MVC thông thường</b> → redirect sang trang lỗi JTE tương ứng</li>
 * </ul>
 *
 * <h3>Exception được xử lý:</h3>
 * <ul>
 *   <li>{@link ResourceNotFoundException} → 404</li>
 *   <li>{@link BusinessException} → 422</li>
 *   <li>{@link MethodArgumentNotValidException} → 400 (Bean Validation)</li>
 *   <li>{@link ConstraintViolationException} → 400 (JPA/method-level validation)</li>
 *   <li>{@link AccessDeniedException} → 403</li>
 *   <li>{@link NoResourceFoundException} → 404 (static resource không tồn tại)</li>
 *   <li>{@link Exception} → 500 (fallback)</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ─────────────────────────────────────────────────────────────────────────
    // 404 – Resource Not Found
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex,
                                         HttpServletRequest req,
                                         HttpServletResponse res) {
        log.warn("404 Not Found: {} [{}]", ex.getMessage(), req.getRequestURI());
        int status = HttpStatus.NOT_FOUND.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.of(status, "Not Found", "RESOURCE_NOT_FOUND",
                            ex.getMessage(), req.getRequestURI()));
        }
        if (isHtmxRequest(req)) {
            return htmxErrorFragment(res, status, ex.getMessage(), "RESOURCE_NOT_FOUND");
        }
        return errorView("pages/404", ex.getMessage(), "RESOURCE_NOT_FOUND", null);
    }

    /** Xử lý khi Spring MVC không tìm thấy static resource (ảnh, JS, CSS...) */
    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFound(NoResourceFoundException ex,
                                        HttpServletRequest req,
                                        HttpServletResponse res) {
        log.debug("404 Static resource not found: {}", req.getRequestURI());
        int status = HttpStatus.NOT_FOUND.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.of(status, "Not Found", "NOT_FOUND",
                            "Không tìm thấy tài nguyên", req.getRequestURI()));
        }
        return errorView("pages/404", "Trang không tồn tại", "NOT_FOUND", null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 422 – Business Logic Error
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException ex,
                                 HttpServletRequest req,
                                 HttpServletResponse res) {
        log.warn("422 Business error [{}]: {}", ex.getCode(), ex.getMessage());
        int status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.of(status, "Unprocessable Entity",
                            ex.getCode(), ex.getMessage(), req.getRequestURI()));
        }
        if (isHtmxRequest(req)) {
            return htmxErrorFragment(res, status, ex.getMessage(), ex.getCode());
        }
        return errorView("pages/error", ex.getMessage(), ex.getCode(), null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 400 – Validation Errors
    // ─────────────────────────────────────────────────────────────────────────

    /** Bean Validation trên @RequestBody / @ModelAttribute */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                               HttpServletRequest req,
                                               HttpServletResponse res) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorResponse.FieldError(fe.getField(),
                        fe.getDefaultMessage()))
                .toList();

        log.warn("400 Validation failed at {}: {} field error(s)",
                req.getRequestURI(), fieldErrors.size());
        int status = HttpStatus.BAD_REQUEST.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.withFieldErrors(status, "Bad Request",
                            "VALIDATION_ERROR", "Dữ liệu không hợp lệ",
                            req.getRequestURI(), fieldErrors));
        }
        if (isHtmxRequest(req)) {
            String summary = fieldErrors.stream()
                    .map(fe -> fe.field() + ": " + fe.message())
                    .findFirst()
                    .orElse("Dữ liệu không hợp lệ");
            return htmxErrorFragment(res, status, summary, "VALIDATION_ERROR");
        }
        // MVC: quay lại form với lỗi (Spring giữ BindingResult trong model)
        ModelAndView mv = new ModelAndView();
        mv.setStatus(HttpStatus.BAD_REQUEST);
        mv.addObject("fieldErrors", fieldErrors);
        return mv;
    }

    /** Validation trên path variable / query param (@Validated) */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException ex,
                                            HttpServletRequest req,
                                            HttpServletResponse res) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(cv -> {
                    String path = cv.getPropertyPath().toString();
                    String field = path.contains(".")
                            ? path.substring(path.lastIndexOf('.') + 1)
                            : path;
                    return new ErrorResponse.FieldError(field, cv.getMessage());
                })
                .toList();

        log.warn("400 Constraint violation at {}: {}", req.getRequestURI(), ex.getMessage());
        int status = HttpStatus.BAD_REQUEST.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.withFieldErrors(status, "Bad Request",
                            "CONSTRAINT_VIOLATION", "Ràng buộc dữ liệu vi phạm",
                            req.getRequestURI(), fieldErrors));
        }
        return errorView("pages/error", "Dữ liệu không hợp lệ", "CONSTRAINT_VIOLATION", null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 403 – Access Denied
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex,
                                     HttpServletRequest req,
                                     HttpServletResponse res) {
        log.warn("403 Access denied: {} {}", req.getMethod(), req.getRequestURI());
        int status = HttpStatus.FORBIDDEN.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.of(status, "Forbidden", "ACCESS_DENIED",
                            "Bạn không có quyền thực hiện hành động này",
                            req.getRequestURI()));
        }
        // Re-throw để Spring Security tự xử lý redirect về /error/403
        // hoặc trả về view trực tiếp
        return errorView("pages/403", "Không có quyền truy cập", "ACCESS_DENIED", null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 500 – Fallback (unexpected errors)
    // ─────────────────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public Object handleUnexpected(Exception ex,
                                   HttpServletRequest req,
                                   HttpServletResponse res) {
        String traceId = generateTraceId();
        log.error("[traceId={}] 500 Unhandled exception at {} {}: {}",
                traceId, req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        res.setStatus(status);

        if (isJsonRequest(req)) {
            return ResponseEntity.status(status)
                    .body(ErrorResponse.of(status, "Internal Server Error",
                            "INTERNAL_ERROR",
                            "Đã có lỗi xảy ra. Mã tra cứu: " + traceId,
                            req.getRequestURI()));
        }
        if (isHtmxRequest(req)) {
            return htmxErrorFragment(res, status,
                    "Lỗi hệ thống (mã: " + traceId + ")", "INTERNAL_ERROR");
        }
        return errorView("pages/500",
                "Đã có lỗi xảy ra. Mã tra cứu: " + traceId,
                "INTERNAL_ERROR", traceId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper Methods
    // ─────────────────────────────────────────────────────────────────────────

    /** Tạo ModelAndView cho trang lỗi JTE. */
    private ModelAndView errorView(String viewName, String message,
                                   String code, String traceId) {
        ModelAndView mv = new ModelAndView(viewName);
        mv.addObject("message", message);
        mv.addObject("code", code);
        if (traceId != null) {
            mv.addObject("traceId", traceId);
        }
        return mv;
    }

    /**
     * Trả về HTML fragment lỗi cho HTMX request.
     * HX-Retarget / HX-Reswap hướng fragment vào vùng toast (#toast-area).
     */
    private ModelAndView htmxErrorFragment(HttpServletResponse res,
                                           int status,
                                           String message,
                                           String code) {
        res.setHeader("HX-Retarget", "#toast-area");
        res.setHeader("HX-Reswap", "beforeend");
        res.setStatus(status);

        ModelAndView mv = new ModelAndView("fragments/toast");
        mv.addObject("message", message);
        mv.addObject("code", code);
        mv.addObject("type", "error");
        return mv;
    }

    /** Kiểm tra request từ HTMX (header HX-Request: true). */
    private boolean isHtmxRequest(HttpServletRequest req) {
        return "true".equalsIgnoreCase(req.getHeader("HX-Request"));
    }

    /** Kiểm tra client muốn nhận JSON (Accept: application/json). */
    private boolean isJsonRequest(HttpServletRequest req) {
        String accept = req.getHeader("Accept");
        return accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Tạo trace ID ngắn gọn (8 ký tự) để log và hiển thị cho người dùng.
     * Trong production nên thay bằng MDC/Micrometer traceId.
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}