package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantDTO save(RestaurantDTO restaurantDTO);

    RestaurantDTO update(RestaurantDTO restaurantDTO);

    RestaurantDTO findById(UUID id);

    RestaurantDTO findByName(String name);

    RestaurantDTO findByEmail(String email);

    List<RestaurantDTO> findAll();

    List<RestaurantDTO> findAllByRestaurantManagerId(UUID id);

    List<RestaurantDTO> findAllByCountryAndCity(String country, String city);

    void delete(UUID id);

    void assignRestaurantManager(UUID restaurantId, UUID managerId);

    void uploadMenu(UUID restaurantId, MultipartFile menuFile);

    void addToFavourite(UUID restaurantId, UUID customerId);

    void removeFromFavourites(UUID restaurantId, UUID customerId);

    List<RestaurantDTO> getFavouriteRestaurants(UUID customerId);
}
