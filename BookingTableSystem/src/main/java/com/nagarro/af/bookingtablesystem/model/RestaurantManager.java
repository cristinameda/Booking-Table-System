package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_managers")
public class RestaurantManager extends User {
    @OneToMany(mappedBy = "restaurantManager", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantManager() {
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city, Restaurant restaurant) {
        super(username, password, fullName, email, phoneNo, country, city);
        this.restaurants.add(restaurant);
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city, List<Restaurant> restaurants) {
        super(username, password, fullName, email, phoneNo, country, city);
        this.restaurants.addAll(restaurants);
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}