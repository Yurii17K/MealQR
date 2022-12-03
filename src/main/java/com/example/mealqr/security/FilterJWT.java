package com.example.mealqr.security;

import com.example.mealqr.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterJWT extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;
    private final RestaurantRepository restaurantRepository;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse,
            @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        final String jwtToken;
        final CustomPrincipal customPrincipal;

        if (authorizationHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            jwtToken = extractToken(authorizationHeader);
            customPrincipal = JWT.extractCustomPrincipal(jwtToken, myUserDetailsService::loadUserByUsername);
            initializeAuthentication(customPrincipal, httpServletRequest);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void initializeAuthentication(@NotNull CustomPrincipal customPrincipal, @NotNull HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                customPrincipal, null, customPrincipal.getAuthoritiesAll(restaurantRepository));
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader.contains("Bearer")) {
            return authorizationHeader.replace("Bearer ", "");
        } else {
            return authorizationHeader;
        }
    }
}