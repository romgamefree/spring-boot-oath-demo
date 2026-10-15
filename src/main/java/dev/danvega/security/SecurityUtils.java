package dev.danvega.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

/**
 * Utility class cung cấp các helper method tĩnh cho Spring Security.
 *
 * <p>Sử dụng ở bất kỳ đâu cần thông tin về user hiện tại mà không cần
 * inject {@code Authentication} vào method signature.</p>
 *
 * <h3>Ví dụ:</h3>
 * <pre>{@code
 * String actor = SecurityUtils.getCurrentUsername();
 * if (SecurityUtils.isAdmin()) { ... }
 * }</pre>
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // utility class – không khởi tạo
    }

    // ── Authentication Info ──────────────────────────────────────────────────

    /**
     * Trả về {@link Authentication} hiện tại, hoặc {@link Optional#empty()} nếu chưa xác thực.
     */
    public static Optional<Authentication> getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || isAnonymous(auth)) {
            return Optional.empty();
        }
        return Optional.of(auth);
    }

    /**
     * Trả về username của user hiện tại.
     * Hỗ trợ cả form-login ({@link UserDetails}) và OAuth2 ({@link OAuth2User}).
     *
     * @return username hoặc {@code "anonymous"} nếu chưa đăng nhập
     */
    public static String getCurrentUsername() {
        return getAuthentication()
                .map(auth -> switch (auth.getPrincipal()) {
                    case UserDetails ud  -> ud.getUsername();
                    case OAuth2User ou  -> resolveOAuth2Name(ou);
                    default             -> auth.getName();
                })
                .orElse("anonymous");
    }

    /**
     * Trả về email của OAuth2 user, hoặc empty nếu không có.
     */
    public static Optional<String> getCurrentUserEmail() {
        return getAuthentication()
                .filter(auth -> auth.getPrincipal() instanceof OAuth2User)
                .map(auth -> ((OAuth2User) auth.getPrincipal()).getAttribute("email"));
    }

    // ── Role / Permission Checks ─────────────────────────────────────────────

    /**
     * Kiểm tra user hiện tại có xác thực hay không.
     */
    public static boolean isAuthenticated() {
        return getAuthentication().isPresent();
    }

    /**
     * Kiểm tra user hiện tại có role ADMIN hay không.
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * Kiểm tra user hiện tại có role cụ thể hay không.
     *
     * @param role tên role bao gồm prefix, ví dụ {@code "ROLE_ADMIN"}
     */
    public static boolean hasRole(String role) {
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return getAuthentication()
                .stream()
                .flatMap(auth -> auth.getAuthorities().stream())
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(role) || a.equals(roleWithPrefix));
    }

    /**
     * Kiểm tra user hiện tại có permission/authority cụ thể hay không.
     *
     * @param authority tên quyền, ví dụ {@code "article:write"}, {@code "user:delete"}
     */
    public static boolean hasAuthority(String authority) {
        return getAuthentication()
                .stream()
                .flatMap(auth -> auth.getAuthorities().stream())
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equalsIgnoreCase);
    }

    /**
     * Shortcut alias cho hasAuthority.
     */
    public static boolean hasPermission(String permission) {
        return hasAuthority(permission);
    }

    /**
     * Lấy SecurityUser nếu đang đăng nhập qua form-login.
     */
    public static Optional<SecurityUser> getCurrentSecurityUser() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(p -> p instanceof SecurityUser)
                .map(p -> (SecurityUser) p);
    }

    // ── Private Helpers ──────────────────────────────────────────────────────

    private static boolean isAnonymous(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ANONYMOUS"::equals);
    }

    private static String resolveOAuth2Name(OAuth2User user) {
        // Ưu tiên: name → login (GitHub) → email → sub
        if (user.getAttribute("name") instanceof String name && !name.isBlank()) return name;
        if (user.getAttribute("login") instanceof String login) return login;
        if (user.getAttribute("email") instanceof String email) return email;
        return user.getName();
    }
}
