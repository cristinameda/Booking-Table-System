package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.ContactMessageDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/contact-messages")
public interface ContactMessageController {
    @PostMapping
    @ApiOperation(value = "Save a new contact message.",
            response = UserResponse.class,
            notes = "Return the message.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message recieved and saved!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<ContactMessageDTO> save(@RequestBody ContactMessageDTO contactMessage);
}
