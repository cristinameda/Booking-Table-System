package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;

import java.util.List;

public interface CustomerService extends UserService<CustomerDTO> {
    void saveCustomersXML(List<CustomerDTO> customers);
}