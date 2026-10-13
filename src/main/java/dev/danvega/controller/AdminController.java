package dev.danvega.controller;

import dev.danvega.service.ArticleService;
import dev.danvega.service.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final ArticleService articleService;
    private final PageService pageService;

    public AdminController(ArticleService articleService, PageService pageService) {
        this.articleService = articleService;
        this.pageService = pageService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("totalArticles", articleService.findAll().size());
        model.addAttribute("totalPages", pageService.findAll().size());
        model.addAttribute("publishedArticles", articleService.findPublished().size());
        model.addAttribute("publishedPages", pageService.findPublished().size());
        return "admin/dashboard";
    }
}
