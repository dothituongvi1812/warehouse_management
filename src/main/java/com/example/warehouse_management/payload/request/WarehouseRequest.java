package com.example.warehouse_management.payload.request;

import com.example.warehouse_management.models.warehouse.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseRequest {
    private String name;
    private Location location;
    private Double length;
    private Double width;
    private Double height;
    private Double widthShelf;
    private Double heightShelf;
    private Double lengthShelf;
    private Integer numberOfFloor;

}
