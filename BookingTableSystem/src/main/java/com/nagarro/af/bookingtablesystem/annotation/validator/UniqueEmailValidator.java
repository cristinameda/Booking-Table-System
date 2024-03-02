package com.nagarro.af.bookingtablesystem.annotation.validator;

import com.nagarro.af.bookingtablesystem.annotation.UniqueEmail;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final RestaurantManagerService restaurantManager;
    private final RestaurantService restaurantService;

    public UniqueEmailValidator(AdminService adminService, CustomerService customerService, RestaurantManagerService restaurantManager, RestaurantService restaurantService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.restaurantManager = restaurantManager;
        this.restaurantService = restaurantService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        boolean isFound = true;
        try {
            adminService.findByEmail(email);
        } catch (NotFoundException e) {
            isFound = false;
        }
        try {
            customerService.findByEmail(email);
        } catch (NotFoundException e) {
            isFound = false;
        }
        try {
            restaurantManager.findByEmail(email);
        } catch (NotFoundException e) {
            isFound = false;
        }
        try {
            restaurantService.findByEmail(email);
        } catch (NotFoundException e) {
            isFound = false;
        }
        return !isFound;
    }
}
