package com.nagarro.af.bookingtablesystem.controller.response;

public class AdminResponse extends UserResponse {

    public AdminResponse() {
    }

    public AdminResponse(String id, String username, String fullName, String email, String phoneNo, String country, String city) {
        super(id, username, fullName, email, phoneNo, country, city);
    }
}
