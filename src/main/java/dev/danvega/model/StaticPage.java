package dev.danvega.model;

import dev.danvega.common.entity.BaseEntity;
import dev.danvega.common.validation.ValidSlug;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity trang tĩnh (StaticPage).
 *
 * <p>Kế thừa {@link BaseEntity} → tự động có: id, createdAt, updatedAt,
 * createdBy, lastModifiedBy.</p>
 */
@Entity
@Table(name = "pages", indexes = {
        @Index(name = "idx_page_slug",      columnList = "slug",      unique = true),
        @Index(name = "idx_page_published", columnList = "published")
})
public class StaticPage extends BaseEntity {

    @ValidSlug(required = true)
    @NotBlank(message = "Slug không được để trống")
    @Column(nullable = false, unique = true)
    private String slug;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean published = false;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
