package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {
}
