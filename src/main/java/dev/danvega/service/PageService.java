package dev.danvega.service;

import dev.danvega.common.service.BaseService;
import dev.danvega.common.web.PagedResult;
import dev.danvega.exception.ResourceNotFoundException;
import dev.danvega.model.StaticPage;
import dev.danvega.repository.PageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý nghiệp vụ cho {@link StaticPage}.
 *
 * <p>Kế thừa {@link BaseService} → tự động có: findAll, findById (với exception),
 * save, deleteById (với exception), count, existsById.</p>
 */
@Service
@Transactional(readOnly = true)
public class PageService extends BaseService<StaticPage, Long> {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        super(pageRepository, "StaticPage");
        this.pageRepository = pageRepository;
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    /**
     * Lấy danh sách trang đã published, sắp xếp theo tiêu đề.
     */
    public List<StaticPage> findPublished() {
        return pageRepository.findByPublishedTrueOrderByTitleAsc(Pageable.unpaged()).getContent();
    }

    /**
     * Tìm kiếm và lọc trang với phân trang.
     */
    public PagedResult<StaticPage> searchPages(String search, Boolean published, Pageable pageable) {
        return PagedResult.from(pageRepository.searchPages(search, published, pageable));
    }

    /**
     * Tìm trang published theo slug.
     *
     * @throws ResourceNotFoundException khi slug không tồn tại hoặc chưa published
     */
    public StaticPage findBySlug(String slug) {
        return pageRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("StaticPage", "slug", slug));
    }

    // ── Write operations ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public StaticPage save(StaticPage page) {
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
