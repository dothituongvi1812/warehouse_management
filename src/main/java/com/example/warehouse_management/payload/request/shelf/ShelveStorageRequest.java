package com.example.warehouse_management.payload.request.shelf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShelveStorageRequest {

    private String name;
    private String code;
    private double width;
    private double length;
    private double height;
    private int numberOfFloor;
    private String warehouseCode;
}
