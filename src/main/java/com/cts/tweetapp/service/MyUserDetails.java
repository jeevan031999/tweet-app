package com.cts.tweetapp.service;

import com.cts.tweetapp.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final User user;

    public MyUserDetails(User user) {
        super();
        this.user = user;
    }

    public String getFirstname() {
        return user.getFirstName();
    }


    public String getLastname() {
        return user.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    public String getLoginId() {
        return user.getLoginId();
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
