package dev.danvega.service;

import dev.danvega.common.service.BaseService;
import dev.danvega.common.web.PagedResult;
import dev.danvega.exception.BusinessException;
import dev.danvega.model.Permission;
import dev.danvega.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PermissionService extends BaseService<Permission, Long> {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        super(permissionRepository, "Permission");
        this.permissionRepository = permissionRepository;
    }

    public PagedResult<Permission> searchPermissions(String search, String module, Pageable pageable) {
        Page<Permission> page;
        if (search != null && !search.isBlank() && module != null && !module.isBlank()) {
            page = permissionRepository.findByNameContainingIgnoreCaseAndModuleContainingIgnoreCase(search, module, pageable);
        } else if (search != null && !search.isBlank()) {
            page = permissionRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (module != null && !module.isBlank()) {
            page = permissionRepository.findByModule(module, pageable);
        } else {
            page = permissionRepository.findAll(pageable);
        }
        return PagedResult.from(page);
    }

    @Transactional
    public Permission createPermission(Permission permission) {
        if (permission.getName() == null || permission.getName().isBlank()) {
            throw new BusinessException("INVALID_PERMISSION_NAME", "Tên quyền không được để trống");
        }
        String cleanName = permission.getName().trim();
        if (permissionRepository.findByName(cleanName).isPresent()) {
            throw new BusinessException("DUPLICATE_PERMISSION_NAME", "Tên quyền '" + cleanName + "' đã tồn tại");
        }
        if (permission.getModule() == null || permission.getModule().isBlank()) {
            throw new BusinessException("INVALID_MODULE", "Module không được để trống");
        }

        permission.setName(cleanName);
        permission.setModule(permission.getModule().trim());

        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission updatePermission(Long id, Permission input) {
        Permission permission = findById(id);

        if (input.getName() != null && !input.getName().isBlank()) {
            String cleanName = input.getName().trim();
            if (!cleanName.equalsIgnoreCase(permission.getName()) && permissionRepository.findByName(cleanName).isPresent()) {
                throw new BusinessException("DUPLICATE_PERMISSION_NAME", "Tên quyền '" + cleanName + "' đã tồn tại");
            }
            permission.setName(cleanName);
        }

        if (input.getDescription() != null) {
            permission.setDescription(input.getDescription());
        }

        if (input.getModule() != null && !input.getModule().isBlank()) {
            permission.setModule(input.getModule().trim());
        }

        return permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}
