package com.nagarro.af.bookingtablesystem.annotation.validator;

import com.nagarro.af.bookingtablesystem.annotation.UniqueUsername;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final RestaurantManagerService managerService;

    public UniqueUsernameValidator(AdminService adminService, CustomerService customerService, RestaurantManagerService managerService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.managerService = managerService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        boolean isFound = true;
        try {
            adminService.findByUsername(username);
        } catch (NotFoundException e) {
            isFound = false;
        }
        try {
            customerService.findByUsername(username);
        } catch (NotFoundException e) {
            isFound = false;
        }
        try {
            managerService.findByUsername(username);
        } catch (NotFoundException e) {
            isFound = false;
        }
        return !isFound;
    }
}
