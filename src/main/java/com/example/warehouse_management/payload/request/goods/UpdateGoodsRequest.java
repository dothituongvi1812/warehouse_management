package com.example.warehouse_management.payload.request.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoodsRequest {
    private String name;
    private double height;
    private double width;
    private double length;
    private String image;
    private String categoryCode;
}
