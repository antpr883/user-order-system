package com.userorder.controller.swagger.api;


import com.userorder.controller.swagger.model.ErrorResponse;
import com.userorder.controller.swagger.model.ValidationErrorResponse;
import com.userorder.service.dto.AddressDTO;
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
 * API interface for Address management
 */
@Tag(name = "Address", description = "Address management APIs")
public interface AddressControllerEndpoint {

    /**
     * GET /api/addresses : Get all addresses with configurable options
     *
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @Operation(
        summary = "Get all addresses with configurable options",
        description = "Returns all addresses with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<AddressDTO>> getAllAddresses(
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit);

    /**
     * GET /api/addresses/:id : Get an address by ID with configurable options
     *
     * @param id the id of the address to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @return the ResponseEntity with status 200 (OK) and the address in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get an address by ID with configurable options",
        description = "Retrieves an address by its ID with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<AddressDTO> getAddress(
            @Parameter(description = "ID of the address to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit);

    /**
     * GET /api/addresses/user/:userId : Get all addresses for a user with configurable options
     *
     * @param userId the id of the user
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @Operation(
        summary = "Get all addresses for a user with configurable options",
        description = "Returns all addresses for a user with optional audit information and specified attributes"
    )
    @GetMapping("/user/{userId}")
    ResponseEntity<List<AddressDTO>> getAddressesByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable @NotNull @Min(1) Long userId,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit);

    /**
     * POST /api/addresses : Create a new address
     *
     * @param addressDTO the address to create
     * @return the ResponseEntity with status 201 (Created) and the new address in body
     */
    @Operation(
        summary = "Create a new address",
        description = "Creates a new address entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Address created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<AddressDTO> createAddress(
            @Parameter(description = "Address data", required = true)
            @Valid @RequestBody AddressDTO addressDTO);

    /**
     * PUT /api/addresses/:id : Update an existing address
     *
     * @param id the id of the address to update
     * @param addressDTO the address to update
     * @return the ResponseEntity with status 200 (OK) and the updated address in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing address",
        description = "Updates an address entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Address updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "ID of the address to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Address data", required = true)
            @Valid @RequestBody AddressDTO addressDTO);

    /**
     * DELETE /api/addresses/:id : Delete the address with the specified id
     *
     * @param id the id of the address to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete an address",
        description = "Deletes an address by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Address deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID of the address to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}