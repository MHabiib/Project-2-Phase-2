package com.future.tcfm.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JwtUserDetails implements UserDetails {

    private String email;
    private String token;
    private String id;
    private String refreshToken;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String email, String id, String token, List<GrantedAuthority> grantedAuthorities) {
        this.email = email;
        this.id = id;
        this.token= token;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    public String getUserName() {
        return email;
    }
    public String getToken() {
        return token;
    }
    public String getId() {
        return id;
    }
}