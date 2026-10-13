package dev.danvega.controller;

import dev.danvega.model.StaticPage;
import dev.danvega.service.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PublicPageController {

    private final PageService pageService;

    public PublicPageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/pages/{slug}")
    public String pageDetail(@PathVariable String slug, Model model) {
        StaticPage page = pageService.findBySlug(slug);
//        if (page.isEmpty()) {
//            return "redirect:/";
//        }
        model.addAttribute("page", page);
        return "web/page";
    }
}
