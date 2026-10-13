package dev.danvega.model;

import dev.danvega.common.entity.BaseEntity;
import dev.danvega.common.validation.ValidSlug;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity bài viết (Article).
 *
 * <p>Kế thừa {@link BaseEntity} → tự động có: id, createdAt, updatedAt,
 * createdBy, lastModifiedBy (không cần khai báo lại).</p>
 *
 * <h3>Thêm field mới:</h3>
 * <pre>{@code
 * @Column(nullable = false)
 * private String category;
 * }</pre>
 */
@Entity
@Table(name = "articles", indexes = {
        @Index(name = "idx_article_slug",      columnList = "slug",      unique = true),
        @Index(name = "idx_article_published", columnList = "published"),
        @Index(name = "idx_article_author",    columnList = "author")
})
public class Article extends BaseEntity {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ValidSlug
    @Column(unique = true)
    private String slug;

    @NotBlank(message = "Tác giả không được để trống")
    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private Boolean published = false;

    // ── Lifecycle hooks ───────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        if (slug == null || slug.isBlank()) {
            slug = generateSlug(title);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // slug không tự thay đổi khi update để giữ URL ổn định
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Tạo slug từ title: lowercase, thay khoảng trắng bằng dash, bỏ ký tự đặc biệt.
     * Override {@code slug} field trước khi persist nếu muốn slug tùy chỉnh.
     */
    private static String generateSlug(String title) {
        if (title == null) return "";
        return title.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
