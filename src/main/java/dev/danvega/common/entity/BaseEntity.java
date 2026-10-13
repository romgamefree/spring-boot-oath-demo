package dev.danvega.common.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Abstract base entity cung cấp các trường audit chung cho tất cả entities.
 *
 * <p>Mọi entity trong dự án nên kế thừa class này để tự động có:</p>
 * <ul>
 *   <li>{@code id} – khóa chính tự tăng</li>
 *   <li>{@code createdAt} – thời điểm tạo (tự động, không thay đổi)</li>
 *   <li>{@code updatedAt} – thời điểm cập nhật cuối (tự động)</li>
 *   <li>{@code createdBy} – username tạo (từ SecurityContext qua AuditConfig)</li>
 *   <li>{@code lastModifiedBy} – username cập nhật cuối</li>
 * </ul>
 *
 * <h3>Cách tạo entity mới:</h3>
 * <pre>{@code
 * @Entity
 * @Table(name = "products")
 * public class Product extends BaseEntity {
 *     @Column(nullable = false)
 *     private String name;
 *     // ... các field nghiệp vụ khác
 * }
 * }</pre>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 100)
    private String lastModifiedBy;

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
}
