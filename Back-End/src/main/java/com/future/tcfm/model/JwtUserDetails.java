package com.future.tcfm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "JwtUserDetails")
public class JwtUserDetails implements UserDetails {
    @Id
    private String id;
    private String email;
    private String token; // ini yang digunakan sbg akses Token
    private String refreshToken;
    private String groupName;
    private Long refreshTokenExpiredAt;
    private Collection<? extends GrantedAuthority> authorities;
    private Long lastModifiedAt;
    public JwtUserDetails(String email,String token,String refreshToken, Long refTokenExpAt,List<GrantedAuthority> grantedAuthorities){
        this.email = email;
        this.token= token;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refTokenExpAt;
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
    public String getUserName() {
        return email;
    }
    public String getToken() {
        return token;
    }
}