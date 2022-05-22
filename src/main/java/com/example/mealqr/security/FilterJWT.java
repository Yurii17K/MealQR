package com.example.mealqr.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@AllArgsConstructor
public class FilterJWT extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String jwtToken = httpServletRequest.getHeader("Authorization");

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtToken.length() != 0 && jwtToken.matches(".*\\..*\\..*")) { // check if token is not empty and has a form of a jwt token
                final String userEmail = JWT.extractAllClaims(jwtToken).getSubject();
                final MyUserDetails myUserDetails = myUserDetailsService.loadUserByUsername(userEmail);

                if (JWT.validateToken(jwtToken, myUserDetails)) {
                    // spring security default bs
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}