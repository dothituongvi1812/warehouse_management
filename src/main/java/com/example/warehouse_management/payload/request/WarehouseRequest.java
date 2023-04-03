package com.example.warehouse_management.payload.request;

import com.example.warehouse_management.models.warehouse.Location;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Getter
@Setter
public class WarehouseRequest {
    private String name;
    private Location location;
    @DecimalMin(value = "0.1",message = "Length is not valid")
    private double length;
    @DecimalMin(value = "0.1",message = "Width is not valid")
    private double width;
    @DecimalMin(value = "0.1",message = "Height is not valid")
    private double height;
    @DecimalMin(value = "0.1",message = "WidthShelf is not valid")
    private double widthShelf;// 1m tối đa các kệ chỉ có 1 kích thước

    @DecimalMin(value = "0.1",message = "HeightShelf is not valid")
    private double heightShelf;//
    @DecimalMin(value = "0.1",message = "LengthShelf is not valid")
    private double lengthShelf;
    @Digits(message = "Number of floor must be a number", integer = 8, fraction = 0)
    private int numberOfFloor;
    @DecimalMin(value = "0.1",message = "LengthOfColumn is not valid")
    private double lengthOfColumn;

}
