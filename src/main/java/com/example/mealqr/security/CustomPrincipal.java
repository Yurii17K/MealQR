package com.example.mealqr.security;

import com.example.mealqr.domain.User;
import com.example.mealqr.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomPrincipal implements UserDetails {

    private final User subject;
    private Set<String> restaurantIds;
    private String promoCodeId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(subject.getRole().name()));
        authorities.add(new SimpleGrantedAuthority(subject.getEmail()));
        return authorities;
    }

    public Collection<SimpleGrantedAuthority> getAuthoritiesAll(RestaurantRepository restaurantRepository) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        restaurantIds = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(subject.getRole().name()));
        authorities.add(new SimpleGrantedAuthority(subject.getEmail()));

        restaurantRepository.findAllByRestaurantManagerEmail(subject.getEmail())//
                .map(restaurant -> {
                    restaurantIds.add(restaurant.getRestaurantId());
                    authorities.add(new SimpleGrantedAuthority(restaurant.getRestaurantId()));
                    return restaurant;
                });

        return authorities;
    }

    @Override
    public String getPassword() {
        return subject.getPass();
    }

    @Override
    public String getUsername() {
        return subject.getEmail();
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

    public Set<String> getRestaurantIds() {
        return this.restaurantIds;
    }

    public String getPromoCodeId() {
        return this.promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }
}
