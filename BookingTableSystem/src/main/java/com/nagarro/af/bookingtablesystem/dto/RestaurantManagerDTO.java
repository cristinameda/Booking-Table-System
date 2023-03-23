package com.nagarro.af.bookingtablesystem.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public class RestaurantManagerDTO extends UserDTO {
    @NotEmpty(message = "Managed restaurants are mandatory!")
    private List<UUID> restaurantIds;

    public RestaurantManagerDTO(UUID id, String username, String password, String fullName, String email, String phoneNo,
                                String country, String city, List<UUID> restaurantIds) {
        super(id, username, password, fullName, email, phoneNo, country, city);
        this.restaurantIds = restaurantIds;
    }

    public List<UUID> getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(List<UUID> restaurantIds) {
        this.restaurantIds = restaurantIds;
    }
}
