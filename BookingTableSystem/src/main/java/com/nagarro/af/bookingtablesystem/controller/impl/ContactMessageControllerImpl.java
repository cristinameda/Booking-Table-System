package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.ContactMessageController;
import com.nagarro.af.bookingtablesystem.dto.ContactMessageDTO;
import com.nagarro.af.bookingtablesystem.service.ContactMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"}, allowCredentials = "true")
public class ContactMessageControllerImpl implements ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageControllerImpl(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @Override
    public ResponseEntity<ContactMessageDTO> save(ContactMessageDTO contactMessage) {
        return new ResponseEntity<>(contactMessageService.save(contactMessage), HttpStatus.OK);
    }
}
