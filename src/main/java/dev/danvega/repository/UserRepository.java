package dev.danvega.repository;

import dev.danvega.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           " LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:roleId IS NULL OR r.id = :roleId) AND " +
           "(:enabled IS NULL OR u.enabled = :enabled)")
    Page<User> searchUsers(@Param("search") String search,
                           @Param("roleId") Long roleId,
                           @Param("enabled") Boolean enabled,
                           Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.username")
    List<User> findAllWithRoles();

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.id = :roleId")
    long countUsersByRoleId(@Param("roleId") Long roleId);
}
