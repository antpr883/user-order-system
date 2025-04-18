package com.userorder.controller;


import com.userorder.controller.swagger.api.ContactControllerEndpoint;
import com.userorder.service.ContactService;
import com.userorder.service.dto.ContactDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.userorder.service.utils.ParamUtils.parseAttributesParam;


/**
 * REST controller for managing Contact entities
 */
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Validated
public class ContactController implements ContactControllerEndpoint {

    private final ContactService contactService;

    /**
     * GET /api/contacts : Get all contacts with configurable options
     *
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<ContactDTO> contacts = contactService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(contacts);
    }

    /**
     * GET /api/contacts/:id : Get a contact by ID with configurable options
     *
     * @param id the id of the contact to retrieve
     * @param withAudit If true, include audit information
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit) {

        ContactDTO contact = contactService.findById(id, withAudit , null);
        return ResponseEntity.ok(contact);
    }


    /**
     * GET /api/contacts/user/:userId : Get all contacts for a user with configurable options
     *
     * @param userId the id of the user
     * @param withAudit If true, include audit information
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactDTO>> getContactsByUserId(
            @PathVariable @NotNull @Min(1) Long userId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit) {

        List<ContactDTO> contacts = contactService.findByUserId(userId, withAudit);
        return ResponseEntity.ok(contacts);
    }

    /**
     * POST /api/contacts : Create a new contact
     *
     * @param contactDTO the contact to create
     * @return the ResponseEntity with status 201 (Created) and the new contact in body
     */
    @Override
    @PostMapping
    public ResponseEntity<ContactDTO> createContact(
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO result = contactService.save(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/contacts/:id : Update an existing contact
     *
     * @param id the id of the contact to update
     * @param contactDTO the contact to update
     * @return the ResponseEntity with status 200 (OK) and the updated contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO result = contactService.update(id, contactDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/contacts/:id : Delete the contact with the specified id
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable @NotNull @Min(1) Long id) {
        contactService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}