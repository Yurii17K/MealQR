package com.example.mealqr.security;

import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.RestaurantEmployeeRepository;
import com.example.mealqr.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantEmployeeRepository restaurantEmployeeRepository;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isPresent()) {
            return new MyUserDetails(optionalUser.get(), restaurantEmployeeRepository);
        } else {
            throw new UsernameNotFoundException("Could not find user");
        }

    }
}
