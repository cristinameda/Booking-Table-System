package com.nagarro.af.bookingtablesystem.controller.response;

public class RestaurantManagerResponse extends UserResponse {

    public RestaurantManagerResponse() {
    }

    public RestaurantManagerResponse(String id, String username, String fullName, String email, String phoneNo, String country, String city) {
        super(id, username, fullName, email, phoneNo, country, city);
    }
}
