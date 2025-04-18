package com.userorder.controller;


import com.userorder.controller.swagger.api.AddressControllerEndpoint;
import com.userorder.service.AddressService;
import com.userorder.service.dto.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Address entities
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController implements AddressControllerEndpoint {

    private final AddressService addressService;

    @Override
    public ResponseEntity<List<AddressDTO>> getAllAddresses(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit) {

        List<AddressDTO> addresses = addressService.findAll(withAudit, null);
        return ResponseEntity.ok(addresses);
    }


    @Override
    public ResponseEntity<AddressDTO> getAddress(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit) {

        AddressDTO address = addressService.findById(id, withAudit, null);
        return ResponseEntity.ok(address);
    }



    @Override
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(
            @PathVariable @NotNull @Min(1) Long userId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit) {

        List<AddressDTO> addresses = addressService.findByUserId(userId, withAudit);
        return ResponseEntity.ok(addresses);
    }


    @Override
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO result = addressService.save(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO result = addressService.update(id, addressDTO);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(
            @PathVariable @NotNull @Min(1) Long id) {
        addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}