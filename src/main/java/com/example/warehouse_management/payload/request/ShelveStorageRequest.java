package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShelveStorageRequest {

    private String name;
    private String code;
    private Double width;
    private Double length;
    private Double height;
    private Integer numberOfFloor;
    private String warehouseCode;
}
