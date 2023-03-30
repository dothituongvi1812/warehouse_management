package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequest {
    private String name;
    private double height;
    private double width;
    private double length;
    private String unit;
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
