package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {
    private Long id;
    private String code;
    private Location location;
    private String name;
    private EStatusStorage status;
    private Double length;
    private Double width;
    private Double height;
    private Double acreage;
    private Double volume;
}
