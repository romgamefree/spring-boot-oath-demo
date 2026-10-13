package dev.danvega.service;

import dev.danvega.common.service.BaseService;
import dev.danvega.common.web.PagedResult;
import dev.danvega.exception.BusinessException;
import dev.danvega.exception.ResourceNotFoundException;
import dev.danvega.model.Article;
import dev.danvega.repository.ArticleRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý nghiệp vụ cho {@link Article}.
 *
 * <p>Kế thừa {@link BaseService} → tự động có: findAll, findById (với exception),
 * save, deleteById (với exception), count, existsById.</p>
 *
 * <p>Chỉ cần thêm các method nghiệp vụ đặc thù của Article.</p>
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends BaseService<Article, Long> {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        super(articleRepository, "Article");
        this.articleRepository = articleRepository;
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    /**
     * Lấy danh sách bài viết đã published, sắp xếp mới nhất lên đầu.
     */
    public List<Article> findPublished() {
        return articleRepository.findByPublishedTrueOrderByCreatedAtDesc(Pageable.unpaged())
                .getContent();
    }

    /**
     * Tìm kiếm và lọc bài viết với phân trang.
     *
     * @param search    từ khóa tìm trong title/content (null = không lọc)
     * @param published lọc theo trạng thái published (null = tất cả)
     */
    public PagedResult<Article> searchArticles(String search, Boolean published, Pageable pageable) {
        return PagedResult.from(articleRepository.searchArticles(search, published, pageable));
    }

    /**
     * Tìm bài viết theo slug.
     *
     * @throws ResourceNotFoundException khi slug không tồn tại
     */
    public Article findBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug);
        if (article == null) {
            throw new ResourceNotFoundException("Article", "slug", slug);
        }
        return article;
    }

    // ── Write operations ──────────────────────────────────────────────────────

    /**
     * Lưu bài viết. Kiểm tra duplicate slug khi tạo mới.
     *
     * @throws BusinessException khi slug đã tồn tại
     */
    @Override
    @Transactional
    public Article save(Article article) {
        if (article.getId() == null && article.getSlug() != null && !article.getSlug().isBlank()) {
            Article existing = articleRepository.findBySlug(article.getSlug());
            if (existing != null) {
                throw new BusinessException("DUPLICATE_SLUG",
                        "Slug '" + article.getSlug() + "' đã được sử dụng");
            }
        }
        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    // ── Background tasks ──────────────────────────────────────────────────────

    /**
     * Import bài viết hàng loạt — tối đa 5 tác vụ đồng thời.
     * Sử dụng @Async nếu muốn chạy nền.
     */
    @ConcurrencyLimit(5)
    public void importArticles() {
        // TODO: implement import logic
    }
}
