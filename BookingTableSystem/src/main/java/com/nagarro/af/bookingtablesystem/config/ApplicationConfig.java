package com.nagarro.af.bookingtablesystem.config;

import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.handler.TradeWebSocketHandler;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Optional;

@Configuration
@EnableWebSocket
@EnableWebMvc
public class ApplicationConfig implements WebSocketConfigurer {

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerRepository restaurantManagerRepository;

    public ApplicationConfig(AdminRepository adminRepository,
                             CustomerRepository customerRepository,
                             RestaurantManagerRepository restaurantManagerRepository) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
            Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
            Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByUsername(username);

            if (optionalAdmin.isPresent()) {
                return optionalAdmin.get();
            }
            if (optionalCustomer.isPresent()) {
                return optionalCustomer.get();
            }
            if (optionalRestaurantManager.isPresent()) {
                return optionalRestaurantManager.get();
            }

            throw new NotFoundException("User with username " + username + " could not be found!");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(tradeWebSocketHandler(), "/logged-users").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler tradeWebSocketHandler() {
        return new TradeWebSocketHandler();
    }
}
