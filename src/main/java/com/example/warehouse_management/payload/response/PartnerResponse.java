package com.example.warehouse_management.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse {

    private Long id;
    private String code;
    private String name;
    private String address;
    private String phone;

}
