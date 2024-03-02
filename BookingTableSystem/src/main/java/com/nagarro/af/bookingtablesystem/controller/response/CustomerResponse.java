package com.nagarro.af.bookingtablesystem.controller.response;

public class CustomerResponse extends UserResponse {

    public CustomerResponse() {
    }

    public CustomerResponse(String id, String username, String fullName, String email, String phoneNo, String country, String city) {
        super(id, username, fullName, email, phoneNo, country, city);
    }
}
