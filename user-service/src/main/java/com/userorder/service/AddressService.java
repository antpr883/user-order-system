package com.userorder.service;

import com.userorder.persistance.model.Address;
import com.userorder.persistance.model.AddressType;
import com.userorder.service.dto.AddressDTO;

import java.util.List;

public interface AddressService extends BaseService<Address, AddressDTO> {
    List<AddressDTO> findByUserId(Long userId);
    AddressDTO findDefaultAddressByUserId(Long userId);
    List<AddressDTO> findByUserIdAndType(Long userId, AddressType type);
    AddressDTO addAddressToUser(Long userId, AddressDTO addressDTO);
    AddressDTO updateUserAddress(Long userId, Long addressId, AddressDTO addressDTO);
    void deleteUserAddress(Long userId, Long addressId);
    AddressDTO setDefaultAddress(Long userId, Long addressId);
}