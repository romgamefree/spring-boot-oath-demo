package dev.danvega.controller;

import dev.danvega.common.web.FlashMessage;
import dev.danvega.common.web.HtmxUtils;
import dev.danvega.config.CsrfHiddenInput;
import dev.danvega.dto.ArticleCreateRequest;
import dev.danvega.dto.ArticleUpdateRequest;
import dev.danvega.model.Article;
import dev.danvega.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý bài viết trong admin panel.
 *
 * <p>Hỗ trợ đồng thời:</p>
 * <ul>
 *   <li>Request thông thường → full page render</li>
 *   <li>HTMX request → chỉ trả về HTML fragment (table rows)</li>
 * </ul>
 */
@Controller
@RequestMapping("/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean published,
            HttpServletRequest request,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        model.addAttribute("articles", articleService.searchArticles(search, published, pageable));
        model.addAttribute("search",    search);
        model.addAttribute("published", published);

        return HtmxUtils.resolveView(request,
                "admin/articles/table-rows",
                "admin/articles/list");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/articles/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        // ResourceNotFoundException → GlobalExceptionHandler → 404 nếu không tìm thấy
        model.addAttribute("article", articleService.findById(id));
        return "admin/articles/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute ArticleCreateRequest request, RedirectAttributes ra) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSlug(request.getSlug());
        article.setAuthor(request.getAuthor());
        article.setPublished(request.getPublished());
        articleService.save(article);
        FlashMessage.success("Bài viết \"" + article.getTitle() + "\" đã được tạo thành công").addTo(ra);
        return "redirect:/admin/articles";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute ArticleUpdateRequest request,
                         RedirectAttributes ra) {
        Article article = articleService.findById(id);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSlug(request.getSlug());
        article.setAuthor(request.getAuthor());
        article.setPublished(request.getPublished());
        articleService.save(article);
        FlashMessage.success("Bài viết đã được cập nhật").addTo(ra);
        return "redirect:/admin/articles";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "0")  int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) String search,
                         @RequestParam(required = false) Boolean published,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Model model,
                         RedirectAttributes ra) {
        articleService.deleteById(id);

        if (HtmxUtils.isHtmxRequest(request)) {
            HtmxUtils.triggerToast(response, "Bài viết đã được xóa", "success");
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            model.addAttribute("articles", articleService.searchArticles(search, published, pageable));
            model.addAttribute("search",    search);
            model.addAttribute("published", published);
            return "admin/articles/table-rows";
        }

        FlashMessage.success("Bài viết đã được xóa").addTo(ra);
        return "redirect:/admin/articles";
    }
}
