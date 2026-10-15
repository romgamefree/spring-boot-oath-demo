package dev.danvega.init;

import dev.danvega.model.Permission;
import dev.danvega.model.Role;
import dev.danvega.model.User;
import dev.danvega.repository.PermissionRepository;
import dev.danvega.repository.RoleRepository;
import dev.danvega.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class UserRolePermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking and initializing Permissions, Roles, and Default Users...");

        // 1. Initialize Permissions
        Map<String, Permission> permissions = initPermissions();

        // 2. Initialize Roles with Permissions
        Map<String, Role> roles = initRoles(permissions);

        // 3. Initialize Default Users
        initUsers(roles);

        log.info("Permissions, Roles, and Default Users initialization completed.");
    }

    private Map<String, Permission> initPermissions() {
        Map<String, Permission> map = new HashMap<>();

        createPermissionIfAbsent(map, "article:read", "Xem danh sách và chi tiết bài viết", "ARTICLE");
        createPermissionIfAbsent(map, "article:write", "Tạo mới và cập nhật bài viết", "ARTICLE");
        createPermissionIfAbsent(map, "article:delete", "Xóa bài viết", "ARTICLE");

        createPermissionIfAbsent(map, "page:read", "Xem danh sách và nội dung trang tĩnh", "PAGE");
        createPermissionIfAbsent(map, "page:write", "Tạo mới và cập nhật trang tĩnh", "PAGE");
        createPermissionIfAbsent(map, "page:delete", "Xóa trang tĩnh", "PAGE");

        createPermissionIfAbsent(map, "user:read", "Xem danh sách người dùng", "USER");
        createPermissionIfAbsent(map, "user:write", "Tạo mới và cập nhật người dùng, gán vai trò", "USER");
        createPermissionIfAbsent(map, "user:delete", "Xóa tài khoản người dùng", "USER");

        return map;
    }

    private void createPermissionIfAbsent(Map<String, Permission> map, String name, String desc, String module) {
        Permission permission = permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name, desc, module)));
        map.put(name, permission);
    }

    private Map<String, Role> initRoles(Map<String, Permission> perms) {
        Map<String, Role> map = new HashMap<>();

        // ROLE_ADMIN: Full access
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role("ROLE_ADMIN", "Quản trị viên toàn quyền hệ thống");
            r.setPermissions(new HashSet<>(perms.values()));
            return roleRepository.save(r);
        });
        if (adminRole.getPermissions().size() != perms.size()) {
            adminRole.setPermissions(new HashSet<>(perms.values()));
            adminRole = roleRepository.save(adminRole);
        }
        map.put("ROLE_ADMIN", adminRole);

        // ROLE_EDITOR: Article & Page management + user:read
        Role editorRole = roleRepository.findByName("ROLE_EDITOR").orElseGet(() -> {
            Role r = new Role("ROLE_EDITOR", "Biên tập viên quản lý nội dung");
            Set<Permission> p = new HashSet<>();
            addIfPresent(p, perms, "article:read", "article:write", "article:delete",
                    "page:read", "page:write", "page:delete", "user:read");
            r.setPermissions(p);
            return roleRepository.save(r);
        });
        map.put("ROLE_EDITOR", editorRole);

        // ROLE_USER: Read only articles and pages
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role("ROLE_USER", "Người dùng thông thường");
            Set<Permission> p = new HashSet<>();
            addIfPresent(p, perms, "article:read", "page:read");
            r.setPermissions(p);
            return roleRepository.save(r);
        });
        map.put("ROLE_USER", userRole);

        return map;
    }

    private void addIfPresent(Set<Permission> target, Map<String, Permission> source, String... keys) {
        for (String key : keys) {
            Permission p = source.get(key);
            if (p != null) target.add(p);
        }
    }

    private void initUsers(Map<String, Role> roles) {
        createUserIfAbsent("admin", "admin123", "Quản Trị Viên", "admin@example.com", Set.of(roles.get("ROLE_ADMIN")));
        createUserIfAbsent("editor", "editor123", "Biên Tập Viên", "editor@example.com", Set.of(roles.get("ROLE_EDITOR")));
        createUserIfAbsent("user", "user123", "Người Dùng", "user@example.com", Set.of(roles.get("ROLE_USER")));
    }

    private void createUserIfAbsent(String username, String rawPassword, String fullName, String email, Set<Role> userRoles) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setEnabled(true);
            user.setRoles(new HashSet<>(userRoles));
            userRepository.save(user);
            log.info("Created default user: {} (roles: {})", username, userRoles.stream().map(Role::getName).toList());
        }
    }
}
