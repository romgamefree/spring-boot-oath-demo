package dev.danvega.repository;

import dev.danvega.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    List<Permission> findByModule(String module);
    List<Permission> findAllByOrderByModuleAscNameAsc();
    Page<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Permission> findByModule(String module, Pageable pageable);
    Page<Permission> findByNameContainingIgnoreCaseAndModuleContainingIgnoreCase(String name, String module, Pageable pageable);
}
