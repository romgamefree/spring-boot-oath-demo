package dev.danvega.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lỗi nghiệp vụ (business logic) có thể hiển thị trực tiếp cho người dùng.
 * Ánh xạ sang HTTP 422 Unprocessable Entity.
 *
 * <p>Ví dụ sử dụng:</p>
 * <pre>{@code
 * throw new BusinessException("DUPLICATE_SLUG", "Slug '" + slug + "' đã tồn tại");
 * throw new BusinessException("ARTICLE_PUBLISHED", "Không thể xóa bài viết đã xuất bản");
 * }</pre>
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

    /** Mã lỗi ngắn gọn để client phân biệt loại lỗi (ví dụ: DUPLICATE_SLUG) */
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
