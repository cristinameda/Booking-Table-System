package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.CustomerController;
import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.CustomerDTOMapper;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"}, allowCredentials = "true")
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    private final CustomerDTOMapper customerDTOMapper;

    public CustomerControllerImpl(CustomerService customerService, CustomerDTOMapper customerDTOMapper) {
        this.customerService = customerService;
        this.customerDTOMapper = customerDTOMapper;
    }

    @Override
    public ResponseEntity<CustomerResponse> save(CustomerDTO customerDTO) {
        return new ResponseEntity<>(mapToCustomerResponse(customerService.save(customerDTO)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CustomerResponse> update(CustomerDTO customerDTO) {
        return new ResponseEntity<>(mapToCustomerResponse(customerService.update(customerDTO)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomerResponse> findById(UUID id) {
        return new ResponseEntity<>(mapToCustomerResponse(customerService.findById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return new ResponseEntity<>(mapToCustomerResponseList(customerService.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> findAllByFullName(String name) {
        return new ResponseEntity<>(mapToCustomerResponseList(customerService.findAllByFullName(name)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        customerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> saveCustomersXML(List<CustomerDTO> customers) {
        customerService.saveCustomersXML(customers);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private CustomerResponse mapToCustomerResponse(CustomerDTO customerDTO) {
        return customerDTOMapper.mapDTOToResponse(customerDTO);
    }

    private List<CustomerResponse> mapToCustomerResponseList(List<CustomerDTO> customerDTOList) {
        return customerDTOMapper.mapDTOListToResponseList(customerDTOList);
    }
}
