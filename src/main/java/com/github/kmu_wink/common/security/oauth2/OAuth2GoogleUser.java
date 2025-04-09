package com.github.kmu_wink.common.security.oauth2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.github.kmu_wink.domain.user.schema.User;

import lombok.Getter;

@Getter
public class OAuth2GoogleUser implements OAuth2User, UserDetails {

    private final User user;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public OAuth2GoogleUser(OAuth2User user) {

        this.user = null;
        this.attributes = user.getAttributes();
        this.authorities = user.getAuthorities();
    }

    public OAuth2GoogleUser(User user) {

        this.user = user;
        this.attributes = Map.of();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {

        return "";
    }

    @Override
    public String getUsername() {

        return "test1";
    }

    @Override
    public boolean isAccountNonExpired() {

        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {

        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {

        return UserDetails.super.isEnabled();
    }

    @Override
    public String getName() {

        return (String) attributes.get("name");
    }

    public String getEmail() {

        return (String) attributes.get("email");
    }

    public String getSocialId() {

        return (String) attributes.get("sub");
    }
}