package com.example.warehouse_management.payload.request;

import com.example.warehouse_management.models.type.EUnit;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsRequest {
    private String name;
    private Double height;
    private Double width;
    private Double length;
    private String categoryCode;

}
