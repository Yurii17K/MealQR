package com.example.mealqr.security;

import com.example.mealqr.repositories.RestaurantRepository;
import com.example.mealqr.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)//
                .map(user -> new MyUserDetails(user, restaurantRepository))//
                .getOrElseThrow(RuntimeException::new);
    }
}