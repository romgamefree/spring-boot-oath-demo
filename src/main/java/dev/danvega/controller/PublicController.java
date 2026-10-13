package dev.danvega.controller;

import dev.danvega.model.Article;
import dev.danvega.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicController {

    private final ArticleService articleService;

    public PublicController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articles", articleService.findPublished());
        return "web/home";
    }

    @GetMapping("/articles/{slug}")
    public String articleDetail(@PathVariable String slug, Model model) {
        Article article = articleService.findBySlug(slug);
        if (article == null || !article.getPublished()) {
            return "redirect:/";
        }
        model.addAttribute("article", article);
        return "web/article";
    }
}
