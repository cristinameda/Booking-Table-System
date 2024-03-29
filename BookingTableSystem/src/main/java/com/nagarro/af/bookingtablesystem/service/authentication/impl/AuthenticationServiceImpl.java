package com.nagarro.af.bookingtablesystem.service.authentication.impl;

import com.nagarro.af.bookingtablesystem.controller.authentication.request.AuthenticationRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.request.RegisterRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.response.AuthenticationResponse;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.security.JwtService;
import com.nagarro.af.bookingtablesystem.service.authentication.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerRepository restaurantManagerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(AdminRepository adminRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantManagerRepository restaurantManagerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        String jwtToken;
        switch (request.getRole()) {
            case "admin" -> {
                Admin admin = new Admin(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                adminRepository.save(admin);
                jwtToken = jwtService.generateToken(admin);
            }
            case "customer" -> {
                Customer customer = new Customer(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                customerRepository.save(customer);
                jwtToken = jwtService.generateToken(customer);
            }
            case "manager" -> {
                RestaurantManager manager = new RestaurantManager(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                restaurantManagerRepository.save(manager);
                jwtToken = jwtService.generateToken(manager);
            }
            // todo: handle exception
            default -> throw new IllegalStateException("Unexpected role value: " + request.getRole());
        }
        return new AuthenticationResponse(jwtToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(request.getUsername());
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(request.getUsername());
        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByUsername(
                request.getUsername()
        );

        String jwtToken = "";

        if (optionalAdmin.isPresent()) {
            jwtToken = jwtService.generateToken(optionalAdmin.get());
        }

        if (optionalCustomer.isPresent()) {
            jwtToken = jwtService.generateToken(optionalCustomer.get());
        }

        if (optionalRestaurantManager.isPresent()) {
            jwtToken = jwtService.generateToken(optionalRestaurantManager.get());
        }

        if (jwtToken.isEmpty()) {
            // todo: handle exception: say bad credentials or smth + password (see in postman what error will be thrown if password is incorrect and handle it
            throw new NotFoundException("User with username " + request.getUsername() + " could not be found!");
        }

//        PrintWriter writer;
//        try {
//            writer = new PrintWriter(
//                    new FileOutputStream(
//                            this.getClass().getClassLoader().getResource("logged-users.txt").getPath(), true));
//
//            //writer.append(request.getUsername()).append(" logged in at ").append(String.valueOf(LocalDateTime.now())).append("\n");
//            writer.print(request.getUsername());
//            writer.flush();
//            writer.close();
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        }

        String line = request.getUsername() + " logged in at " + LocalDateTime.now() + "\n";

        // Defining the file name of the file
        Path fileName = Path.of(
                "C:/Users/Kitty/Documents/Facultate/Nagarro/AF/af22-meda-titu/BookingTableSystem/src/main/resources/logged-users.txt");

        // Writing into the file
        try {
            Files.writeString(fileName, line, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AuthenticationResponse(jwtToken);
    }
}
