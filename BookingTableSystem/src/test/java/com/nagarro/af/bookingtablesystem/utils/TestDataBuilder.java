package com.nagarro.af.bookingtablesystem.utils;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataBuilder {
    public static final String RESTAURANT_ID = "3f81c2f1-2225-4cbe-9be9-59e5f2fe426a";
    public static final String RESTAURANT_CAPACITY_ID = "3f81c2f1-2225-4cbe-9be9-59e5f2fe426a";
    public static final String CUSTOMER_ID = "d0cb12ab-a96b-4b63-bf29-3bedbee80d55";
    public static final String RESTAURANT_MANAGER_ID = "b1810386-ed01-4126-be40-e5b8d46cb763";
    public static final String BOOKING_ID = "663cbc8f-3380-4417-bbf2-769fe401dcaf";

    public static Menu buildMenu(Restaurant restaurant) {
        Path path = Paths.get("src/main/resources/Menu.jpg");
        try {
            byte[] content = java.nio.file.Files.readAllBytes(path);
            return new Menu(
                    "menu/Menu.jpg",
                    content,
                    "image/jpeg",
                    restaurant
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Restaurant buildRestaurant() {
        Restaurant restaurant = new Restaurant(
                "Garlic",
                "garlic@yahoo.com",
                "+40766578102",
                "Romania",
                "Brasov",
                "str. Lazar 18",
                "No vampires for sure!",
                null,
                50,
                15,
                buildRestaurantManager()
        );
        restaurant.setId(UUID.fromString(RESTAURANT_ID));
        return restaurant;
    }

    public static RestaurantDTO buildRestaurantDTO() {
        return new RestaurantDTO(
                UUID.fromString(RESTAURANT_ID),
                "Garlic",
                "garlic@yahoo.com",
                "+40766578102",
                "Romania",
                "Brasov",
                "str. Lazar 18",
                "No vampires for sure!",
                null,
                50,
                15,
                UUID.fromString(RESTAURANT_MANAGER_ID)
        );
    }

    public static Customer buildCustomer() {
        Customer customer = new Customer(
                "customerone",
                "Test123!",
                "Customer One",
                "customer_one@yahoo.com",
                "+40765124990",
                "Romania",
                "Brasov"
        );
        customer.setId(UUID.fromString(CUSTOMER_ID));
        return customer;
    }

    public static CustomerDTO buildCustomerDTO() {
        return new CustomerDTO(
                UUID.fromString(CUSTOMER_ID),
                "customerone",
                "Test123!",
                "Customer One",
                "customer_one@yahoo.com",
                "+40765124990",
                "Romania",
                "Brasov"
        );
    }

    public static RestaurantManager buildRestaurantManager() {
        RestaurantManager restaurantManager = new RestaurantManager(
                "manager-garlic",
                "Test123!",
                "Manager Garlic",
                "manager_garlic@yahoo.com",
                "+40789678123",
                "Romania",
                "Brasov"
        );
        restaurantManager.setId(UUID.fromString(RESTAURANT_MANAGER_ID));
        return restaurantManager;
    }

    public static RestaurantManagerDTO buildRestaurantManagerDTO() {
        List<UUID> managedRestaurantsIds = new ArrayList<>();
        managedRestaurantsIds.add(UUID.fromString(RESTAURANT_ID));
        return new RestaurantManagerDTO(
                UUID.fromString(RESTAURANT_MANAGER_ID),
                "manager-garlic",
                "Test123!",
                "Manager Garlic",
                "manager_garlic@yahoo.com",
                "+40789678123",
                "Romania",
                "Brasov",
                managedRestaurantsIds
        );
    }

    public static Booking buildBooking() {
        Booking booking = new Booking(
                buildCustomer(),
                buildRestaurant(),
                LocalDateTime.now(),
                3,
                1
        );
        booking.setId(UUID.fromString(BOOKING_ID));
        return booking;
    }

    public static Booking buildBooking(Restaurant restaurant, Customer customer) {
        Booking booking = new Booking(
                customer,
                restaurant,
                LocalDateTime.now(),
                3,
                1
        );
        booking.setId(UUID.fromString(BOOKING_ID));
        return booking;
    }
}
