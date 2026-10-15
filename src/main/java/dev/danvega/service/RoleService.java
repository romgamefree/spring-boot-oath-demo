package dev.danvega.service;

import dev.danvega.exception.ResourceNotFoundException;
import dev.danvega.model.Permission;
import dev.danvega.model.Role;
import dev.danvega.repository.PermissionRepository;
import dev.danvega.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
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
}
