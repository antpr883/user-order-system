package com.userorder.controller.swagger.api;


import com.userorder.controller.swagger.model.ErrorResponse;
import com.userorder.controller.swagger.model.ValidationErrorResponse;
import com.userorder.service.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API interface for User management
 */
@Tag(name = "User", description = "User management APIs")
public interface UserControllerEndpoint {

    /**
     * GET /api/users : Get all users with configurable options
     *
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @Operation(
        summary = "Get all users with configurable options",
        description = "Returns all users with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<UserDTO>> getAllUsers(
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include (e.g., \"contacts,roles.permissions,addresses\")")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/users/:id : Get the user with configurable options
     *
     * @param id The ID of the user to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return the ResponseEntity with status 200 (OK) and the user in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a user by ID with configurable options",
        description = "Retrieves a user by its ID with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include (e.g., \"contacts,roles.permissions,addresses\")")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * POST /api/users : Create a new user
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and the new user in body
     */
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<UserDTO> createUser(
            @Parameter(description = "User data", required = true)
            @Valid @RequestBody UserDTO userDTO);

    /**
     * PUT /api/users/:id : Update an existing user
     *
     * @param id the id of the user to update
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and the updated user in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing user",
        description = "Updates a user entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "User data", required = true)
            @Valid @RequestBody UserDTO userDTO);

    /**
     * DELETE /api/users/:id : Delete the user with the specified id
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}