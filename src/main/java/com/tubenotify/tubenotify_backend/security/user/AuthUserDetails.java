package com.tubenotify.tubenotify_backend.security.user;

import com.tubenotify.tubenotify_backend.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of UserDetails for Spring Security authentication
 */
@Getter
@Setter
public class AuthUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetails(
            Long id,
            String username,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Builds an AuthUserDetails instance from a User entity
     *
     * @param user the User entity to build from
     * @return AuthUserDetails populated with the user's details and authorities
     */
    public static AuthUserDetails buildUserDetails(User user) {

        GrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole().getRoleName().name());

        return new AuthUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
