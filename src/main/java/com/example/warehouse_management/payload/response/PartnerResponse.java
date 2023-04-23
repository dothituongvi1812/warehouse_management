package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.partner.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse {

    private String code;
    private String name;
    private Address address;
    private String phone;

}
