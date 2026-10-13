package dev.danvega.common.web;

/**
 * Record biểu diễn flash message (success/error/warning/info) dùng trong
 * pattern Post/Redirect/Get (PRG).
 *
 * <h3>Cách sử dụng trong Controller (POST handler):</h3>
 * <pre>{@code
 * @PostMapping
 * public String save(@ModelAttribute Article article, RedirectAttributes ra) {
 *     articleService.save(article);
 *     FlashMessage.success("Bài viết đã được lưu thành công").addTo(ra);
 *     return "redirect:/admin/articles";
 * }
 * }</pre>
 *
 * <h3>Hiển thị trong JTE layout (layout/admin.jte):</h3>
 * <pre>{@code
 * @template.fragments.flash-message(flash = flash)
 * }</pre>
 *
 * @param type    loại message: SUCCESS, ERROR, WARNING, INFO
 * @param message nội dung hiển thị cho người dùng
 */
public record FlashMessage(Type type, String message) {

    public enum Type {
        SUCCESS, ERROR, WARNING, INFO
    }

    /** Attribute name dùng trong RedirectAttributes và Model */
    public static final String FLASH_ATTR = "flash";

    // ── Factory methods ───────────────────────────────────────────────────────

    public static FlashMessage success(String message) {
        return new FlashMessage(Type.SUCCESS, message);
    }

    public static FlashMessage error(String message) {
        return new FlashMessage(Type.ERROR, message);
    }

    public static FlashMessage warning(String message) {
        return new FlashMessage(Type.WARNING, message);
    }

    public static FlashMessage info(String message) {
        return new FlashMessage(Type.INFO, message);
    }

    // ── Helper methods ────────────────────────────────────────────────────────

    /**
     * Thêm flash message vào {@link org.springframework.web.servlet.mvc.support.RedirectAttributes}
     * để hiển thị sau redirect.
     */
    public void addTo(org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        ra.addFlashAttribute(FLASH_ATTR, this);
    }

    /** CSS class Tailwind tương ứng với loại message (dùng trong JTE template). */
    public String cssClass() {
        return switch (type) {
            case SUCCESS -> "bg-green-50 border-green-400 text-green-800";
            case ERROR   -> "bg-red-50 border-red-400 text-red-800";
            case WARNING -> "bg-yellow-50 border-yellow-400 text-yellow-800";
            case INFO    -> "bg-blue-50 border-blue-400 text-blue-800";
        };
    }

    /** Icon emoji tương ứng với loại message. */
    public String icon() {
        return switch (type) {
            case SUCCESS -> "✓";
            case ERROR   -> "✖";
            case WARNING -> "⚠";
            case INFO    -> "ℹ";
        };
    }
}
