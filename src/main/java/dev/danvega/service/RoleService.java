package dev.danvega.service;

import dev.danvega.exception.BusinessException;
import dev.danvega.exception.ResourceNotFoundException;
import dev.danvega.model.Permission;
import dev.danvega.model.Role;
import dev.danvega.repository.PermissionRepository;
import dev.danvega.repository.RoleRepository;
import dev.danvega.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAllWithPermissions();
    }

    public Role findById(Long id) {
        return roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAllByOrderByModuleAscNameAsc();
    }

    public Map<String, List<Permission>> getPermissionsGroupedByModule() {
        return permissionRepository.findAllByOrderByModuleAscNameAsc()
                .stream()
                .collect(Collectors.groupingBy(Permission::getModule));
    }

    @Transactional
    public Role createRole(Role role, Set<Long> permissionIds) {
        if (role.getName() == null || role.getName().isBlank()) {
            throw new BusinessException("INVALID_ROLE_NAME", "Tên vai trò không được để trống");
        }
        String cleanName = role.getName().trim().toUpperCase();
        if (roleRepository.existsByName(cleanName)) {
            throw new BusinessException("DUPLICATE_ROLE_NAME", "Tên vai trò '" + cleanName + "' đã tồn tại");
        }

        role.setName(cleanName);

        if (permissionIds != null && !permissionIds.isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role input, Set<Long> permissionIds) {
        Role role = findById(id);

        if (input.getName() != null && !input.getName().isBlank()) {
            String cleanName = input.getName().trim().toUpperCase();
            if (!cleanName.equalsIgnoreCase(role.getName()) && roleRepository.existsByName(cleanName)) {
                throw new BusinessException("DUPLICATE_ROLE_NAME", "Tên vai trò '" + cleanName + "' đã tồn tại");
            }
            role.setName(cleanName);
        }

        role.setDescription(input.getDescription());

        if (permissionIds != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = findById(id);
        if ("ROLE_ADMIN".equalsIgnoreCase(role.getName()) || "ROLE_USER".equalsIgnoreCase(role.getName())) {
            throw new BusinessException("CANNOT_DELETE_SYSTEM_ROLE", "Không thể xóa vai trò hệ thống mặc định");
        }
        
        long userCount = userRepository.countUsersByRoleId(id);
        if (userCount > 0) {
            throw new BusinessException("ROLE_IN_USE", "Không thể xóa vai trò này vì đang được sử dụng bởi " + userCount + " người dùng");
        }
        
        roleRepository.deleteById(id);
    }
}
