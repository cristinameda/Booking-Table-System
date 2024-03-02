package com.nagarro.af.bookingtablesystem.controller.response;

import java.util.Objects;

public class UserResponse {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNo;
    private String country;
    private String city;

    public UserResponse() {
    }

    public UserResponse(String id, String username, String fullName, String email, String phoneNo, String country, String city) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(fullName, that.fullName) &&
                Objects.equals(email, that.email) && Objects.equals(phoneNo, that.phoneNo) && Objects.equals(country, that.country) &&
                Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, fullName, email, phoneNo, country, city);
    }
}
