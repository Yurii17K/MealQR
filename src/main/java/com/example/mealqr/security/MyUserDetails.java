package com.example.mealqr.security;

import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.RestaurantEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class MyUserDetails implements UserDetails {

    private final User user;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        authorities.add(new SimpleGrantedAuthority(user.getEmail()));

        restaurantEmployeeRepository
                .findByUserEmail(user.getEmail())
                .ifPresent(restaurantEmployee -> authorities.add(new SimpleGrantedAuthority(restaurantEmployee.getRestaurantName())));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
