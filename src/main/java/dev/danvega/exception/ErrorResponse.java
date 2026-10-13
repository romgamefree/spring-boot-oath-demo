package dev.danvega.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

/**
 * Cấu trúc response lỗi chuẩn hóa cho REST API (JSON).
 *
 * <p>Ví dụ response khi validation thất bại:</p>
 * <pre>{@code
 * {
 *   "timestamp": "2026-10-13T04:30:00Z",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "code": "VALIDATION_ERROR",
 *   "message": "Dữ liệu không hợp lệ",
 *   "path": "/api/articles",
 *   "fieldErrors": [
 *     { "field": "title", "message": "Tiêu đề không được để trống" }
 *   ]
 * }
 * }</pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        List<FieldError> fieldErrors
) {

    /** Lỗi chi tiết từng field (dùng cho validation) */
    public record FieldError(String field, String message) {}

    // ── Factory methods ───────────────────────────────────────────────────────

    public static ErrorResponse of(int status, String error, String code,
                                   String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, code, message, path, null);
    }

    public static ErrorResponse withFieldErrors(int status, String error, String code,
                                                String message, String path,
                                                List<FieldError> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, code, message, path, fieldErrors);
    }
}
