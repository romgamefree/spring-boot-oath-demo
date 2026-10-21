package dev.danvega.service;

import dev.danvega.common.service.BaseService;
import dev.danvega.common.web.PagedResult;
import dev.danvega.exception.BusinessException;
import dev.danvega.model.Role;
import dev.danvega.model.User;
import dev.danvega.repository.RoleRepository;
import dev.danvega.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseService<User, Long> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, "User");
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PagedResult<User> searchUsers(String search, Long roleId, Boolean enabled, Pageable pageable) {
        return PagedResult.from(userRepository.searchUsers(search, roleId, enabled, pageable));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(User user, String rawPassword, Set<Long> roleIds) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BusinessException("INVALID_USERNAME", "Tên đăng nhập không được để trống");
        }
        String cleanUsername = user.getUsername().trim().toLowerCase();
        if (userRepository.existsByUsername(cleanUsername)) {
            throw new BusinessException("DUPLICATE_USERNAME", "Tên đăng nhập '" + cleanUsername + "' đã tồn tại");
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            String cleanEmail = user.getEmail().trim().toLowerCase();
            if (userRepository.existsByEmail(cleanEmail)) {
                throw new BusinessException("DUPLICATE_EMAIL", "Email '" + cleanEmail + "' đã được sử dụng");
            }
            user.setEmail(cleanEmail);
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new BusinessException("INVALID_PASSWORD", "Mật khẩu phải có ít nhất 6 ký tự");
        }

        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(rawPassword));

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);
        } else {
            // Default role: ROLE_USER
            roleRepository.findByName("ROLE_USER").ifPresent(r -> user.setRoles(Set.of(r)));
        }

        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User input, String newPassword, Set<Long> roleIds) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new dev.danvega.exception.ResourceNotFoundException("User", "id", id));

        if (input.getEmail() != null && !input.getEmail().isBlank()) {
            String cleanEmail = input.getEmail().trim().toLowerCase();
            if (!cleanEmail.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(cleanEmail)) {
                throw new BusinessException("DUPLICATE_EMAIL", "Email '" + cleanEmail + "' đã được sử dụng");
            }
            user.setEmail(cleanEmail);
        } else {
            user.setEmail(null);
        }

        user.setFullName(input.getFullName());

        if (input.getEnabled() != null) {
            user.setEnabled(input.getEnabled());
        }

        if (newPassword != null && !newPassword.isBlank()) {
            if (newPassword.length() < 6) {
                throw new BusinessException("INVALID_PASSWORD", "Mật khẩu mới phải có ít nhất 6 ký tự");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        if (roleIds != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User toggleEnabled(Long id, String currentUsername) {
        User user = findById(id);
        if (user.getUsername().equalsIgnoreCase(currentUsername)) {
            throw new BusinessException("CANNOT_DISABLE_SELF", "Bạn không thể tự vô hiệu hóa tài khoản của chính mình");
        }
        user.setEnabled(!Boolean.TRUE.equals(user.getEnabled()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id, String currentUsername) {
        User user = findById(id);
        if (user.getUsername().equalsIgnoreCase(currentUsername)) {
            throw new BusinessException("CANNOT_DELETE_SELF", "Bạn không thể tự xóa tài khoản của chính mình");
        }
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            throw new BusinessException("CANNOT_DELETE_ADMIN", "Không thể xóa tài khoản admin mặc định");
        }
        userRepository.deleteById(id);
    }
}
