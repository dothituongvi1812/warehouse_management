package com.example.warehouse_management.payload.request;

import com.example.warehouse_management.models.warehouse.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseRequest {
    private String name;
    private Location location;
    private double length;
    private double width;
    private double height;
    private double widthShelf;// 1m tối đa các kệ chỉ có 1 kích thước
    private double heightShelf;//
    private double lengthShelf;
    private int numberOfFloor;

}
