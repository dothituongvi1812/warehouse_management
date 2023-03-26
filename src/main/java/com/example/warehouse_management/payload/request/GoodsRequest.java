package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsRequest {
    private String name;
    private double height;
    private double width;
    private double length;
    private String unit;
    private int quantity;
    private String categoryCode;

}
