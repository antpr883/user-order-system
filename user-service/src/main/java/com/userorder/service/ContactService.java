package com.userorder.service;

import com.userorder.persistance.model.Contact;
import com.userorder.service.dto.ContactDTO;

import java.util.List;

public interface ContactService extends BaseService<Contact, ContactDTO> {
    List<ContactDTO> findByUserId(Long userId);
    ContactDTO findPrimaryContactByUserId(Long userId);
    ContactDTO addContactToUser(Long userId, ContactDTO contactDTO);
    ContactDTO updateUserContact(Long userId, Long contactId, ContactDTO contactDTO);
    void deleteUserContact(Long userId, Long contactId);
    ContactDTO setPrimaryContact(Long userId, Long contactId);
    ContactDTO verifyContact(Long userId, Long contactId);
}