package com.example.mealqr.security;

import com.example.mealqr.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomPrincipal loadUserByUsername(String subject) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(subject)//
                .map(CustomPrincipal::new)//
                .getOrElseThrow(() -> new UsernameNotFoundException("Subject not found"));
    }
}