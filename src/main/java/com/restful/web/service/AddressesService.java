package com.restful.web.service;

import com.restful.web.shared.dto.AddressDTO;

import java.util.List;

public interface AddressesService {

    List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
