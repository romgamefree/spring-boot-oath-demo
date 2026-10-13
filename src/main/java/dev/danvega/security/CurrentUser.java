package dev.danvega.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Shortcut annotation để inject principal của user đang đăng nhập.
 *
 * <p>Thay vì viết dài:</p>
 * <pre>{@code
 * public String profile(@AuthenticationPrincipal Object principal) { ... }
 * }</pre>
 *
 * <p>Dùng gọn:</p>
 * <pre>{@code
 * public String profile(@CurrentUser Object principal) { ... }
 * }</pre>
 *
 * <p>Kết hợp với SecurityUtils để lấy thông tin chi tiết:</p>
 * <pre>{@code
 * @GetMapping("/profile")
 * public String profile(@CurrentUser Object principal, Model model) {
 *     model.addAttribute("username", SecurityUtils.getCurrentUsername());
 *     return "pages/profile";
 * }
 * }</pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
