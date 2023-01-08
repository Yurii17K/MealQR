package com.example.mealqr.security;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static Option<String> getCurrentUserLogin() {
        return Option.of(SecurityContextHolder.getContext().getAuthentication())//
                .map(Authentication::getPrincipal)//
                .map(principal -> {
                    if (principal instanceof String) {
                        return (String) principal;
                    } else {
                        return ((CustomPrincipal) principal).getUsername();
                    }
                });
    }
}
