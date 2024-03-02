package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.RestaurantController;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"},
        allowCredentials = "true")
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantControllerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public ResponseEntity<RestaurantDTO> save(RestaurantDTO restaurantDTO) {
        return new ResponseEntity<>(restaurantService.save(restaurantDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RestaurantDTO> update(RestaurantDTO restaurantDTO) {
        return new ResponseEntity<>(restaurantService.update(restaurantDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RestaurantDTO> findById(UUID id) {
        return new ResponseEntity<>(restaurantService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RestaurantDTO> findByName(String name) {
        return new ResponseEntity<>(restaurantService.findByName(name), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> findAll() {
        return new ResponseEntity<>(restaurantService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> findAllByRestaurantManagerId(UUID id) {
        return new ResponseEntity<>(restaurantService.findAllByRestaurantManagerId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> findAllByCountryAndCity(String country, String city) {
        return new ResponseEntity<>(restaurantService.findAllByCountryAndCity(country, city), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        restaurantService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> assignRestaurantManager(UUID restaurantId, UUID managerId) {
        restaurantService.assignRestaurantManager(restaurantId, managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadMenu(UUID restaurantId, MultipartFile menu) {
        restaurantService.uploadMenu(restaurantId, menu);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addToFavourite(UUID restaurantId, UUID customerId) {
        restaurantService.addToFavourite(restaurantId, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removeFromFavourites(UUID restaurantId, UUID customerId) {
        restaurantService.removeFromFavourites(restaurantId, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> getFavouriteRestaurants(UUID customerId) {
        return new ResponseEntity<>(restaurantService.getFavouriteRestaurants(customerId), HttpStatus.OK);
    }
}
