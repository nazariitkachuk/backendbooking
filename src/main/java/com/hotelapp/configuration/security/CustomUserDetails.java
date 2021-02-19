package com.hotelapp.configuration.security;

import com.hotelapp.entities.Role;
import com.hotelapp.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class implementing UserDetails interface
 */
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private List<GrantedAuthority> authorities;


    public org.springframework.security.core.userdetails.User getUser() {

        return new org.springframework.security.core.userdetails.User(
                this.username, this.password, isEnabled(),
                isAccountNonExpired(), isCredentialsNonExpired(),
                isAccountNonLocked(), this.authorities);
    }

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.id=user.getId();
        this.authorities = user.getRoles().stream()
                .map(Role::getRoleName)
                .map(e -> new SimpleGrantedAuthority(e))
                .collect(Collectors.toList());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return true;
    }
}
