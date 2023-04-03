package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequest {
    private String name;
    @DecimalMin(value = "0.1",message = "Height is not valid")
    private double height;
    @DecimalMin(value = "0.1",message = "Width is not valid")
    private double width;
    @DecimalMin(value = "0.1",message = "Length is not valid")
    private double length;
    @DecimalMin(value = "0.1",message = "Length is not valid")
    private String unit;
    @Digits(message = "Quantity must be a number", integer = 8, fraction = 0)
    private int quantity;
    private String categoryCode;
    private double volume;

    public void setVolume(double width, double height, double length) {
        this.volume = width * height * length;
    }
    public double getVolume() {
        return height * width * length;
    }

}
