package com.nagarro.af.bookingtablesystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**", "/logged-users", "/contact-messages")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/customers/{id}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/restaurants/customer/{customerId}/add-favourites/{restaurantId}").hasAnyAuthority("ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/restaurants/customer/{customerId}/remove-favourites/{restaurantId}").hasAnyAuthority("ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/restaurants/customer/{customerId}/favourites").hasAnyAuthority("ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/managers/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .requestMatchers("/admins/**", "/customers/**", "/managers/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/restaurants").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants/manager/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/restaurants").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/restaurants/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/restaurants/{restaurant_id}/manager/{manager_id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/restaurants/{restaurant_id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                .requestMatchers(HttpMethod.POST, "/bookings").hasAuthority("ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/bookings/restaurant/{restaurant_id}").hasAuthority("ROLE_MANAGER")
                .requestMatchers(HttpMethod.GET, "/bookings/name/{name}").hasAuthority("ROLE_MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/bookings/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
