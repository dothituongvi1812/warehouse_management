package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShelveStorageResponse {
    private String name;
    private String code;
    private int numberOfFloors;
    private double width;
    private double height;
    private double length;
    private String warehouseCode;
    private String warehouseName;
}
