package com.nagarro.af.bookingtablesystem.security;

import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${secret.key}")
    private String SECRET_KEY;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantManagerRepository restaurantManagerRepository;

    public JwtService(AdminRepository adminRepository, CustomerRepository customerRepository, RestaurantManagerRepository restaurantManagerRepository) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Optional<Admin> admin = adminRepository.findByUsername(userDetails.getUsername());
        Optional<Customer> customer = customerRepository.findByUsername(userDetails.getUsername());
        Optional<RestaurantManager> manager = restaurantManagerRepository.findByUsername(userDetails.getUsername());

        String fullName = "";
        String email = "";
        String id = "";
        String role = "";

        if (admin.isPresent()) {
            fullName = admin.get().getFullName();
            email = admin.get().getEmail();
            id = admin.get().getId().toString();
            role = "admin";
        }

        if (customer.isPresent()) {
            fullName = customer.get().getFullName();
            email = customer.get().getEmail();
            id = customer.get().getId().toString();
            role = "customer";
        }

        if (manager.isPresent()) {
            fullName = manager.get().getFullName();
            email = manager.get().getEmail();
            id = manager.get().getId().toString();
            role = "manager";
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("fullName", fullName);
        claims.put("email", email);
        claims.put("role", role);

        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
