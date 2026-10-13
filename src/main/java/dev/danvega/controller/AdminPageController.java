package dev.danvega.controller;

import dev.danvega.common.web.PagedResult;
import dev.danvega.model.StaticPage;
import dev.danvega.service.PageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/pages")
public class AdminPageController {

    private final PageService pageService;

    public AdminPageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean published,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PagedResult<StaticPage> pages = pageService.searchPages(search, published, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("search", search);
        model.addAttribute("published", published);
        if (hxRequest != null) {
            return "admin/pages/table-rows";
        }
        return "admin/pages/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("page", new StaticPage());
        return "admin/pages/form";
    }

    @PostMapping
    public String save(@ModelAttribute StaticPage page) {
        pageService.save(page);
        return "redirect:/admin/pages";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("page", pageService.findById(id));
        return "admin/pages/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute StaticPage page) {
        page.setId(id);
        pageService.save(page);
        return "redirect:/admin/pages";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) Boolean published,
                        @RequestHeader(value = "HX-Request", required = false) String hxRequest,
                        Model model) {
        pageService.deleteById(id);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PagedResult<StaticPage> pages = pageService.searchPages(search, published, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("search", search);
        model.addAttribute("published", published);
        if (hxRequest != null) {
            return "admin/pages/table-rows";
        }
        return "admin/pages/list";
    }
}
