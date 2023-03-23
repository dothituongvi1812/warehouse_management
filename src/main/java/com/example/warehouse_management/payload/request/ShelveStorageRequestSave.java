package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShelveStorageRequestSave {
    private String code;
    private String name;
    private Double width;
    private Double length;
    private Double height;
    private String warehouseCode;
}
