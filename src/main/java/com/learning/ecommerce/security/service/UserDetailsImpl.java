package com.learning.ecommerce.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.ecommerce.entity.Users;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {

    private String username;

    private String email;

    private String name;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String roles;

    
    public UserDetailsImpl(String username, String email, String name, String password, String roles) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public static UserDetailsImpl build (Users user) {
        return new UserDetailsImpl(user.getId(), user.getEmail(), 
        user.getName(), user.getPassword(), user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (StringUtils.hasText(roles)) {
            String[] splits = roles.replaceAll(" ", "").split(",");
            for (String key : splits) {
                authorities.add(new SimpleGrantedAuthority(key));
            }
        }

        return authorities;
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
