package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.type.EStatusStorage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RowLocationResponse {
    private Long id;
    private String codeRow;
    private String nameRow;
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
