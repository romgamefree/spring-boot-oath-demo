package dev.danvega.common.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Utility class cho HTMX integration.
 *
 * <p>Thay thế pattern lặp lại trong mọi controller:</p>
 * <pre>{@code
 * // Thay vì:
 * if (hxRequest != null) { return "fragments/xxx"; }
 *
 * // Dùng:
 * if (HtmxUtils.isHtmxRequest(request)) { return "fragments/xxx"; }
 * }</pre>
 *
 * <h3>HTMX Response Headers:</h3>
 * <ul>
 *   <li>{@code HX-Redirect} – redirect toàn trang từ phía server</li>
 *   <li>{@code HX-Refresh} – buộc browser reload trang</li>
 *   <li>{@code HX-Retarget} – đổi target element swap</li>
 *   <li>{@code HX-Trigger} – kích hoạt custom event phía client</li>
 *   <li>{@code HX-Push-Url} – cập nhật URL trên browser</li>
 * </ul>
 */
public final class HtmxUtils {

    // HTMX Request Headers
    public static final String HX_REQUEST      = "HX-Request";
    public static final String HX_BOOSTED      = "HX-Boosted";
    public static final String HX_TARGET       = "HX-Target";
    public static final String HX_TRIGGER_NAME = "HX-Trigger-Name";

    // HTMX Response Headers
    public static final String HX_REDIRECT  = "HX-Redirect";
    public static final String HX_REFRESH   = "HX-Refresh";
    public static final String HX_RETARGET  = "HX-Retarget";
    public static final String HX_RESWAP    = "HX-Reswap";
    public static final String HX_TRIGGER   = "HX-Trigger";
    public static final String HX_PUSH_URL  = "HX-Push-Url";

    private HtmxUtils() {}

    // ── Request Detection ────────────────────────────────────────────────────

    /**
     * Kiểm tra đây có phải HTMX request không.
     */
    public static boolean isHtmxRequest(HttpServletRequest request) {
        return "true".equalsIgnoreCase(request.getHeader(HX_REQUEST));
    }

    /**
     * Kiểm tra đây có phải HTMX Boost request không.
     */
    public static boolean isBoostedRequest(HttpServletRequest request) {
        return "true".equalsIgnoreCase(request.getHeader(HX_BOOSTED));
    }

    /**
     * Lấy target element ID mà HTMX đang swap vào.
     */
    public static String getTarget(HttpServletRequest request) {
        return request.getHeader(HX_TARGET);
    }

    // ── Response Helpers ─────────────────────────────────────────────────────

    /**
     * Thêm header HX-Redirect để redirect toàn trang sau HTMX request.
     */
    public static void redirect(HttpServletResponse response, String url) {
        response.setHeader(HX_REDIRECT, url);
    }

    /**
     * Thêm header HX-Refresh để buộc browser reload.
     */
    public static void refresh(HttpServletResponse response) {
        response.setHeader(HX_REFRESH, "true");
    }

    /**
     * Thêm header HX-Trigger để kích hoạt custom event phía client.
     *
     * @param eventName tên event, ví dụ {@code "articleSaved"}, {@code "showToast"}
     */
    public static void trigger(HttpServletResponse response, String eventName) {
        response.setHeader(HX_TRIGGER, eventName);
    }

    /**
     * Thêm header HX-Trigger với JSON payload để truyền data kèm event.
     *
     * @param jsonPayload ví dụ: {@code "{\"showToast\": {\"message\": \"Đã lưu\"}}"}
     */
    public static void triggerWithDetail(HttpServletResponse response, String jsonPayload) {
        response.setHeader(HX_TRIGGER, jsonPayload);
    }

    /**
     * Kích hoạt toast qua HTMX HX-Trigger header.
     *
     * @param response HttpServletResponse
     * @param message  nội dung thông báo
     * @param type     loại toast (info, success, warning, error)
     */
    public static void triggerToast(HttpServletResponse response, String message, String type) {
        String safeMsg = message == null ? "" : message.replace("\"", "\\\"");
        String safeType = type == null ? "info" : type.replace("\"", "\\\"");
        triggerWithDetail(response, "{\"showToast\": {\"message\": \"" + safeMsg + "\", \"type\": \"" + safeType + "\"}}");
    }

    /**
     * Cập nhật URL browser history sau HTMX swap.
     */
    public static void pushUrl(HttpServletResponse response, String url) {
        response.setHeader(HX_PUSH_URL, url);
    }

    // ── View Resolution ──────────────────────────────────────────────────────

    /**
     * Trả về view phù hợp: fragment nếu là HTMX request, full view nếu không.
     *
     * <p>Thay thế pattern if/else lặp lại trong controller:</p>
     * <pre>{@code
     * return HtmxUtils.resolveView(request, "admin/articles/table-rows", "admin/articles/list");
     * }</pre>
     *
     * @param request      HttpServletRequest hiện tại
     * @param htmxFragment tên template khi là HTMX request
     * @param fullView     tên template khi là request thông thường
     */
    public static String resolveView(HttpServletRequest request,
                                     String htmxFragment,
                                     String fullView) {
        return isHtmxRequest(request) ? htmxFragment : fullView;
    }

    /**
     * Tạo {@link ModelAndView} cho HTMX request, tự động set Retarget/Reswap nếu cần.
     */
    public static ModelAndView htmxView(HttpServletResponse response,
                                        String fragmentView,
                                        String retarget,
                                        String reswap) {
        if (retarget != null) response.setHeader(HX_RETARGET, retarget);
        if (reswap   != null) response.setHeader(HX_RESWAP, reswap);
        return new ModelAndView(fragmentView);
    }
}
