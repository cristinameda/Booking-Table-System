package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.model.EditGroup;
import com.nagarro.af.bookingtablesystem.model.SaveGroup;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface UserController<D extends UserDTO, R extends UserResponse> {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Save a new user.",
            response = UserResponse.class,
            notes = "Return the created user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully created!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<R> save(@Validated(SaveGroup.class) @RequestBody D userDTO);

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Updates and existing user.",
            response = UserResponse.class,
            notes = "Return the modified user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully updated!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<R> update(@Validated(EditGroup.class) @RequestBody D userDTO);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find user by id.",
            response = UserResponse.class,
            notes = "Return the user with the given id if found.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found!"),
            @ApiResponse(code = 404, message = "User not found!")
    })
    ResponseEntity<R> findById(@PathVariable UUID id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all users.",
            notes = "Return all the saved users.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users returned!")
    })
    ResponseEntity<List<R>> findAll();

    @GetMapping(
            path = "/name/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all users that have the requested name.",
            notes = "Return all the saved users with that name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users returned!")
    })
    ResponseEntity<List<R>> findAllByFullName(@PathVariable String name);

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an existing user by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully deleted!"),
            @ApiResponse(code = 404, message = "User not found!")
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
