package com.example.mealqr.security;

import com.example.mealqr.domain.enums.Roles;
import com.example.mealqr.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FilterJWT filterJWT;
    private final UserRepository userRepository;
    private final Environment environment;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(filterJWT, UsernamePasswordAuthenticationFilter.class);

        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()//
                .antMatchers("/api/users/sign-in", "/api/users/sign-up").permitAll()//
                .antMatchers("/api/users/update-allergies").authenticated()//

                .antMatchers("/api/report-comment").authenticated()//
                .antMatchers("/api/opinion/add-comment").authenticated()//
                .antMatchers("/api/opinion/add-rating").authenticated()//
                .antMatchers("/api/generate-qr").authenticated()//

                .antMatchers(HttpMethod.POST, "/api/dishes").hasAuthority(Roles.RESTAURANT_MANAGER.name())//
                .antMatchers(HttpMethod.PUT, "/api/dishes").hasAuthority(Roles.RESTAURANT_MANAGER.name())//
                .antMatchers(HttpMethod.PATCH, "/api/dishes").hasAuthority(Roles.RESTAURANT_MANAGER.name())//
                .antMatchers(HttpMethod.DELETE, "/api/dishes").hasAuthority(Roles.RESTAURANT_MANAGER.name())//

                .antMatchers("/api/restaurant").hasAuthority(Roles.RESTAURANT_MANAGER.name())//
                .antMatchers("/api/restaurants").hasAuthority(Roles.RESTAURANT_MANAGER.name())//

                .antMatchers("/api/cart/**", "/api/cart**").hasAuthority(Roles.CLIENT.name());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern(environment.getProperty("security.cors.origins.pattern"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}