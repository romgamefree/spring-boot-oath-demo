package dev.danvega.security;

import dev.danvega.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SecurityUser implements UserDetails {

    private final User user;
    private final Set<GrantedAuthority> authorities;

    public SecurityUser(User user) {
        this.user = user;
        this.authorities = new HashSet<>();

        if (user.getRoles() != null) {
            for (var role : user.getRoles()) {
                // Add role authority (e.g. ROLE_ADMIN)
                this.authorities.add(new SimpleGrantedAuthority(role.getName()));

                // Add all permission authorities under this role (e.g. article:read, user:manage)
                if (role.getPermissions() != null) {
                    for (var permission : role.getPermissions()) {
                        this.authorities.add(new SimpleGrantedAuthority(permission.getName()));
                    }
                }
            }
        }
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }
}
