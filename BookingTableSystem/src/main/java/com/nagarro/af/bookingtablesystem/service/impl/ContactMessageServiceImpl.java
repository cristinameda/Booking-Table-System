package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.ContactMessageDTO;
import com.nagarro.af.bookingtablesystem.model.ContactMessage;
import com.nagarro.af.bookingtablesystem.repository.ContactMessageRepository;
import com.nagarro.af.bookingtablesystem.service.ContactMessageService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ModelMapper modelMapper;

    public ContactMessageServiceImpl(ContactMessageRepository contactMessageRepository, ModelMapper modelMapper) {
        this.contactMessageRepository = contactMessageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ContactMessageDTO save(ContactMessageDTO contactMessage) {
        ContactMessage message = modelMapper.map(contactMessage, ContactMessage.class);
        return modelMapper.map(contactMessageRepository.save(message), ContactMessageDTO.class);
    }
}
