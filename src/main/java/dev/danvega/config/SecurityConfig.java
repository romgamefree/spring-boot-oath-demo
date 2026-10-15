package dev.danvega.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/", "/articles/**", "/pages/**", "/login", "/error", "/error/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler((request, response, authentication) -> {
                        org.springframework.web.servlet.FlashMap flashMap = new org.springframework.web.servlet.FlashMap();
                        flashMap.put(dev.danvega.common.web.FlashMessage.FLASH_ATTR,
                                dev.danvega.common.web.FlashMessage.success("Đăng nhập thành công! Chào mừng " + authentication.getName()));
                        new org.springframework.web.servlet.support.SessionFlashMapManager().saveOutputFlashMap(flashMap, request, response);
                        response.sendRedirect("/admin");
                    })
                    .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .successHandler((request, response, authentication) -> {
                        org.springframework.web.servlet.FlashMap flashMap = new org.springframework.web.servlet.FlashMap();
                        flashMap.put(dev.danvega.common.web.FlashMessage.FLASH_ATTR,
                                dev.danvega.common.web.FlashMessage.success("Đăng nhập thành công qua OAuth2!"));
                        new org.springframework.web.servlet.support.SessionFlashMapManager().saveOutputFlashMap(flashMap, request, response);
                        response.sendRedirect("/admin");
                    })
            )
            .logout(logout -> logout
                    .logoutSuccessHandler((request, response, authentication) -> {
                        org.springframework.web.servlet.FlashMap flashMap = new org.springframework.web.servlet.FlashMap();
                        flashMap.put(dev.danvega.common.web.FlashMessage.FLASH_ATTR,
                                dev.danvega.common.web.FlashMessage.info("Bạn đã đăng xuất thành công"));
                        new org.springframework.web.servlet.support.SessionFlashMapManager().saveOutputFlashMap(flashMap, request, response);
                        response.sendRedirect("/login");
                    })
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails defaultUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(defaultUser);
    }
}
