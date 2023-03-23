package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.type.EUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsResponse {

    private String code;
    private String name;
    private String unit;
    private double length;
    private double width;
    private double height;
    private String categoryCode;
    private String categoryName;


}
