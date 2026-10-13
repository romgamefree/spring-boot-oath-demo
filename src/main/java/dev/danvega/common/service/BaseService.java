package dev.danvega.common.service;

import dev.danvega.common.web.PagedResult;
import dev.danvega.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Abstract generic service cung cấp các CRUD operations cơ bản.
 *
 * <p>Service nghiệp vụ chỉ cần kế thừa và override những gì khác biệt:</p>
 *
 * <pre>{@code
 * @Service
 * public class ProductService extends BaseService<Product, Long> {
 *
 *     public ProductService(ProductRepository repository) {
 *         super(repository, "Product");
 *     }
 *
 *     // Chỉ cần thêm method nghiệp vụ đặc thù:
 *     public List<Product> findByCategory(String category) {
 *         return ((ProductRepository) repository).findByCategory(category);
 *     }
 * }
 * }</pre>
 *
 * @param <T>  kiểu Entity
 * @param <ID> kiểu khóa chính (thường là Long)
 */
public abstract class BaseService<T, ID> {

    /** Repository được inject bởi subclass */
    protected final JpaRepository<T, ID> repository;

    /** Tên entity dùng trong error messages (ví dụ: "Article", "Product") */
    private final String entityName;

    protected BaseService(JpaRepository<T, ID> repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    // ── Read operations ───────────────────────────────────────────────────────

    /**
     * Lấy tất cả entities. Dùng khi dữ liệu nhỏ và không cần phân trang.
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Lấy danh sách có phân trang, trả về {@link PagedResult} chuẩn hóa.
     */
    public PagedResult<T> findAll(Pageable pageable) {
        return PagedResult.from(repository.findAll(pageable));
    }

    /**
     * Tìm theo ID, ném {@link ResourceNotFoundException} nếu không tồn tại.
     *
     * @throws ResourceNotFoundException khi không tìm thấy entity
     */
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }

    /**
     * Kiểm tra entity có tồn tại hay không.
     */
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /**
     * Đếm tổng số entities.
     */
    public long count() {
        return repository.count();
    }

    // ── Write operations ──────────────────────────────────────────────────────

    /**
     * Lưu entity (tạo mới hoặc cập nhật).
     * Override nếu cần thêm business logic (validate, slug generation, ...).
     */
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Xóa entity theo ID.
     *
     * @throws ResourceNotFoundException khi không tìm thấy entity cần xóa
     */
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(entityName, "id", id);
        }
        repository.deleteById(id);
    }
}
