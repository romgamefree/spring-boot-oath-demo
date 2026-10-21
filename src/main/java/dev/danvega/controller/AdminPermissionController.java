package dev.danvega.controller;

import dev.danvega.common.web.FlashMessage;
import dev.danvega.common.web.HtmxUtils;
import dev.danvega.common.web.PagedResult;
import dev.danvega.model.Permission;
import dev.danvega.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPermissionController {

    private final PermissionService permissionService;

    public AdminPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String module,
            HttpServletRequest request,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("module").ascending().and(Sort.by("name").ascending()));
        PagedResult<Permission> permissions = permissionService.searchPermissions(search, module, pageable);

        model.addAttribute("permissions", permissions);
        model.addAttribute("search", search);
        model.addAttribute("module", module);

        return HtmxUtils.resolveView(request, "admin/permissions/table-rows", "admin/permissions/list");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("permission", new Permission());
        return "admin/permissions/form";
    }

    @PostMapping
    public String create(@ModelAttribute Permission permission, RedirectAttributes ra) {
        permissionService.createPermission(permission);
        FlashMessage.success("Quyền \"" + permission.getName() + "\" đã được tạo thành công").addTo(ra);
        return "redirect:/admin/permissions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("permission", permissionService.findById(id));
        return "admin/permissions/form";
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(@PathVariable Long id, @ModelAttribute Permission input, RedirectAttributes ra) {
        permissionService.updatePermission(id, input);
        FlashMessage.success("Quyền đã được cập nhật").addTo(ra);
        return "redirect:/admin/permissions";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String module,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            RedirectAttributes ra) {

        permissionService.deletePermission(id);

        if (HtmxUtils.isHtmxRequest(request)) {
            HtmxUtils.triggerToast(response, "Quyền đã được xóa thành công", "success");
            Pageable pageable = PageRequest.of(page, size, Sort.by("module").ascending().and(Sort.by("name").ascending()));
            PagedResult<Permission> permissions = permissionService.searchPermissions(search, module, pageable);
            model.addAttribute("permissions", permissions);
            model.addAttribute("search", search);
            model.addAttribute("module", module);
            return "admin/permissions/table-rows";
        }

        FlashMessage.success("Quyền đã được xóa thành công").addTo(ra);
        return "redirect:/admin/permissions";
    }
}
