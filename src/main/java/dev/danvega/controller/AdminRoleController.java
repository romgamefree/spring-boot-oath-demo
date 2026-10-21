package dev.danvega.controller;

import dev.danvega.common.web.FlashMessage;
import dev.danvega.common.web.HtmxUtils;
import dev.danvega.model.Permission;
import dev.danvega.model.Role;
import dev.danvega.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public String list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String module,
            HttpServletRequest request,
            Model model) {

        List<Role> roles = roleService.findAll();
        Map<String, List<Permission>> permissionsByModule = roleService.getPermissionsGroupedByModule();

        model.addAttribute("roles", roles);
        model.addAttribute("permissionsByModule", permissionsByModule);
        model.addAttribute("search", search);
        model.addAttribute("module", module);

        return HtmxUtils.resolveView(request, "admin/roles/table-rows", "admin/roles/list");
    }

    @GetMapping("/new")
    @Transactional(readOnly = true)
    public String createForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("permissionsByModule", roleService.getPermissionsGroupedByModule());
        return "admin/roles/form";
    }

    @PostMapping
    public String create(
            @ModelAttribute Role role,
            @RequestParam(required = false) Set<Long> permissionIds,
            RedirectAttributes ra) {

        roleService.createRole(role, permissionIds);
        FlashMessage.success("Vai trò \"" + role.getName() + "\" đã được tạo thành công").addTo(ra);
        return "redirect:/admin/roles";
    }

    @GetMapping("/{id}/edit")
    @Transactional(readOnly = true)
    public String editForm(@PathVariable Long id, Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        model.addAttribute("permissionsByModule", roleService.getPermissionsGroupedByModule());
        return "admin/roles/form";
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(
            @PathVariable Long id,
            @ModelAttribute Role input,
            @RequestParam(required = false) Set<Long> permissionIds,
            RedirectAttributes ra) {

        roleService.updateRole(id, input, permissionIds);
        FlashMessage.success("Vai trò đã được cập nhật").addTo(ra);
        return "redirect:/admin/roles";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            RedirectAttributes ra) {

        roleService.deleteRole(id);

        if (HtmxUtils.isHtmxRequest(request)) {
            HtmxUtils.triggerToast(response, "Vai trò đã được xóa thành công", "success");
            List<Role> roles = roleService.findAll();
            Map<String, List<Permission>> permissionsByModule = roleService.getPermissionsGroupedByModule();
            model.addAttribute("roles", roles);
            model.addAttribute("permissionsByModule", permissionsByModule);
            return "admin/roles/table-rows";
        }

        FlashMessage.success("Vai trò đã được xóa thành công").addTo(ra);
        return "redirect:/admin/roles";
    }
}
