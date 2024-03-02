package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/customers")
public interface CustomerController extends UserController<CustomerDTO, CustomerResponse> {
    @PostMapping(
            path = "/xml",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Save customers in an xml format.",
            response = UserResponse.class,
            notes = "Return the user with the given id if found.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customers exported!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<Void> saveCustomersXML(@RequestBody List<CustomerDTO> customers);
}
