package dev.danvega.config;

import dev.danvega.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Cấu hình JPA Auditing tự động điền {@code createdBy} và {@code lastModifiedBy}
 * từ SecurityContext vào mọi entity kế thừa {@link dev.danvega.common.entity.BaseEntity}.
 *
 * <p>Sau khi config này active, các field audit sẽ tự động được điền:</p>
 * <ul>
 *   <li>{@code created_by} – username khi entity được tạo</li>
 *   <li>{@code last_modified_by} – username khi entity được cập nhật</li>
 * </ul>
 *
 * <p>Không cần viết thêm code gì trong Service hay Entity.</p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Cung cấp username hiện tại từ Spring Security cho JPA Auditing.
     * Trả về {@code "system"} nếu chưa xác thực (ví dụ khi chạy CommandLineRunner).
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String username = SecurityUtils.getCurrentUsername();
            // Khi chạy DataInitializer (chưa có security context), dùng "system"
            return Optional.of("anonymous".equals(username) ? "system" : username);
        };
    }
}
