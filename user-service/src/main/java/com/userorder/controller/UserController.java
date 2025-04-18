package com.userorder.controller;


import com.userorder.controller.swagger.api.UserControllerEndpoint;
import com.userorder.service.UserService;
import com.userorder.service.dto.UserDTO;
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
 * REST controller for managing User entities
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController implements UserControllerEndpoint {

    private final UserService userService;

    /**
     * GET /api/users : Get all users with configurable options
     * 
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return ResponseEntity with status 200 (OK) and the list of users in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<UserDTO> users = userService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/:id : Get a user by ID with configurable options
     * 
     * @param id The ID of the user to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return ResponseEntity with status 200 (OK) and the user in body
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        UserDTO user = userService.findById(id, withAudit, attributeSet);
        return ResponseEntity.ok(user);
    }

    /**
     * POST /api/users : Create a new user
     * 
     * @param userDTO The user to create
     * @return ResponseEntity with status 201 (Created) and the new user in body
     */
    @Override
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO result = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/users/:id : Update an existing user
     * 
     * @param id The ID of the user to update
     * @param userDTO The user to update
     * @return ResponseEntity with status 200 (OK) and the updated user in body
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable @NotNull @Min(1) Long id, 
            @Valid @RequestBody UserDTO userDTO) {
        UserDTO result = userService.update(id, userDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/users/:id : Delete a user
     * 
     * @param id The ID of the user to delete
     * @return ResponseEntity with status 204 (No Content)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull @Min(1) Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}