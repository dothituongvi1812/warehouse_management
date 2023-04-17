package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinLocationResponse {
    private Long id;
    private String codeBin;
    private String nameBin;
    private double length;
    private double width;
    private double height;
    private String status;
    private String nameWarehouse;
    private String codeWarehouse;
    private String codeShelf;
    private String nameShelf;
    private String codeColumn;
    private String nameColumn;
    private GoodsResponse goods;
    private int maxCapacity;
    private int currentCapacity;
}
