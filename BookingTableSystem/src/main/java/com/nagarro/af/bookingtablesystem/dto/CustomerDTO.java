package com.nagarro.af.bookingtablesystem.dto;

import java.util.UUID;

public class CustomerDTO extends UserDTO {

    public CustomerDTO(UUID id, String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(id, username, password, fullName, email, phoneNo, country, city);
    }
}
