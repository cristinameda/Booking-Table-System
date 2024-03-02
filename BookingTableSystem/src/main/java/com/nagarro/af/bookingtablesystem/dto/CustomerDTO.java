package com.nagarro.af.bookingtablesystem.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CustomerDTO", namespace = "urn:eventis:crm:2.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDTO extends UserDTO {

    public CustomerDTO() {
    }

    public CustomerDTO(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }
}
