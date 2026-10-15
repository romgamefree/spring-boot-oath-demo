package dev.danvega.controller;

import dev.danvega.common.web.FlashMessage;
import dev.danvega.common.web.HtmxUtils;
import dev.danvega.common.web.PagedResult;
import dev.danvega.config.CsrfHiddenInput;
import dev.danvega.model.Role;
import dev.danvega.model.User;
import dev.danvega.security.SecurityUtils;
import dev.danvega.service.RoleService;
import dev.danvega.service.UserService;
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

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Boolean enabled,
            HttpServletRequest request,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        PagedResult<User> users = userService.searchUsers(search, roleId, enabled, pageable);
        List<Role> roles = roleService.findAll();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("search", search);
        model.addAttribute("roleId", roleId);
        model.addAttribute("enabled", enabled);

        return HtmxUtils.resolveView(request, "admin/users/table-rows", "admin/users/list");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin/users/form";
    }

    @PostMapping
    public String create(
            @ModelAttribute User user,
            @RequestParam String password,
            @RequestParam(required = false) Set<Long> roleIds,
            RedirectAttributes ra) {

        userService.createUser(user, password, roleIds);
        FlashMessage.success("Người dùng \"" + user.getUsername() + "\" đã được tạo thành công").addTo(ra);
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "admin/users/form";
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(
            @PathVariable Long id,
            @ModelAttribute User input,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Set<Long> roleIds,
            RedirectAttributes ra) {

        userService.updateUser(id, input, password, roleIds);
        FlashMessage.success("Thông tin người dùng \"" + input.getUsername() + "\" đã được cập nhật").addTo(ra);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Boolean enabled,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            RedirectAttributes ra) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        User updatedUser = userService.toggleEnabled(id, currentUsername);

        if (HtmxUtils.isHtmxRequest(request)) {
            HtmxUtils.triggerToast(response, "Đã cập nhật trạng thái cho " + updatedUser.getUsername(), "info");
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            PagedResult<User> users = userService.searchUsers(search, roleId, enabled, pageable);
            model.addAttribute("users", users);
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("search", search);
            model.addAttribute("roleId", roleId);
            model.addAttribute("enabled", enabled);
            return "admin/users/table-rows";
        }

        FlashMessage.success("Đã cập nhật trạng thái người dùng").addTo(ra);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Boolean enabled,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            RedirectAttributes ra) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        userService.deleteUser(id, currentUsername);

        if (HtmxUtils.isHtmxRequest(request)) {
            HtmxUtils.triggerToast(response, "Người dùng đã được xóa thành công", "success");
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            PagedResult<User> users = userService.searchUsers(search, roleId, enabled, pageable);
            model.addAttribute("users", users);
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("search", search);
            model.addAttribute("roleId", roleId);
            model.addAttribute("enabled", enabled);
            return "admin/users/table-rows";
        }

        FlashMessage.success("Người dùng đã được xóa thành công").addTo(ra);
        return "redirect:/admin/users";
    }
}
