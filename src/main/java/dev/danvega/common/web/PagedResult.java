package dev.danvega.common.web;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper chuẩn hóa kết quả phân trang, tách khỏi Spring Data Page API.
 *
 * <p>Giúp controller và template không phụ thuộc trực tiếp vào
 * {@link org.springframework.data.domain.Page}, dễ test và dễ thay thế.</p>
 *
 * <h3>Tạo từ Spring Data Page:</h3>
 * <pre>{@code
 * Page<Article> page = repository.findAll(pageable);
 * PagedResult<Article> result = PagedResult.from(page);
 * model.addAttribute("result", result);
 * }</pre>
 *
 * <h3>Dùng trong JTE template:</h3>
 * <pre>{@code
 * @param dev.danvega.common.web.PagedResult<dev.danvega.model.Article> result
 *
 * @for(var article : result.content())
 *     ...
 * @endfor
 *
 * <%-- Pagination --%>
 * @if(result.hasPrevious())
 *     <a hx-get="?page=${result.currentPage() - 1}">Trước</a>
 * @endif
 * }</pre>
 *
 * @param <T> kiểu dữ liệu trong trang
 */
public record PagedResult<T>(
        List<T>  content,
        int      currentPage,
        int      pageSize,
        long     totalElements,
        int      totalPages,
        boolean  first,
        boolean  last
) {

    /**
     * Tạo {@code PagedResult} từ {@link Page} của Spring Data.
     */
    public static <T> PagedResult<T> from(Page<T> page) {
        return new PagedResult<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    /** Kiểm tra có trang trước không. */
    public boolean hasPrevious() {
        return !first;
    }

    /** Kiểm tra có trang tiếp theo không. */
    public boolean hasNext() {
        return !last;
    }

    /** Kiểm tra có nhiều hơn 1 trang không. */
    public boolean isPaged() {
        return totalPages > 1;
    }

    /** Số trang bắt đầu từ 1 (dùng cho hiển thị). */
    public int displayPage() {
        return currentPage + 1;
    }
}
